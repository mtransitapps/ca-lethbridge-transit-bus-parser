package org.mtransit.parser.ca_lethbridge_transit_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.Pair;
import org.mtransit.parser.SplitUtils;
import org.mtransit.parser.SplitUtils.RouteTripSpec;
import org.mtransit.parser.StringUtils;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.gtfs.data.GTripStop;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MDirectionType;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;
import org.mtransit.parser.mt.data.MTripStop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mtransit.parser.StringUtils.EMPTY;

// http://opendata.lethbridge.ca/
// http://opendata.lethbridge.ca/datasets/e5ce3aa182114d66926d06ba732fb668
// https://www.lethbridge.ca/OpenDataSets/GTFS_Transit_Data.zip
public class LethbridgeTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@Nullable String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-lethbridge-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new LethbridgeTransitBusAgencyTools().start(args);
	}

	@Nullable
	private HashSet<Integer> serviceIdInts;

	@Override
	public void start(@NotNull String[] args) {
		MTLog.log("Generating Lethbridge Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIdInts = extractUsefulServiceIdInts(args, this, true);
		super.start(args);
		MTLog.log("Generating Lethbridge Transit bus data... DONE in %s.", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIdInts != null && this.serviceIdInts.isEmpty();
	}

	@Override
	public boolean excludeCalendar(@NotNull GCalendar gCalendar) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarInt(gCalendar, this.serviceIdInts);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(@NotNull GCalendarDate gCalendarDates) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarDateInt(gCalendarDates, this.serviceIdInts);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(@NotNull GTrip gTrip) {
		if (this.serviceIdInts != null) {
			return excludeUselessTripInt(gTrip, this.serviceIdInts);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public boolean excludeRoute(@NotNull GRoute gRoute) {
		return super.excludeRoute(gRoute);
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	private static final String N = "n";
	private static final String S = "s";

	private static final long RID_STARTS_WITH_N = 140_000L;
	private static final long RID_STARTS_WITH_S = 190_000L;

	@Override
	public long getRouteId(@NotNull GRoute gRoute) {
		if (Utils.isDigitsOnly(gRoute.getRouteShortName())) {
			return Long.parseLong(gRoute.getRouteShortName()); // using route short name as route ID
		}
		Matcher matcher = DIGITS.matcher(gRoute.getRouteShortName());
		if (matcher.find()) {
			long digits = Long.parseLong(matcher.group());
			if (gRoute.getRouteShortName().toLowerCase(Locale.ENGLISH).endsWith(N)) {
				return RID_STARTS_WITH_N + digits;
			} else if (gRoute.getRouteShortName().toLowerCase(Locale.ENGLISH).endsWith(S)) {
				return RID_STARTS_WITH_S + digits;
			}
		}
		throw new MTLog.Fatal("Unexpected route ID for %s!", gRoute);
	}

	private static final String _SLASH_ = " / ";

	@NotNull
	@SuppressWarnings("DuplicateBranchesInSwitch")
	@Override
	public String getRouteLongName(@NotNull GRoute gRoute) {
		String routeLongName = gRoute.getRouteLongNameOrDefault();
		if (StringUtils.isEmpty(routeLongName)) {
			routeLongName = gRoute.getRouteDescOrDefault(); // using route description as route long name
		}
		if (StringUtils.isEmpty(routeLongName)) {
			if (Utils.isDigitsOnly(gRoute.getRouteShortName())) {
				int rsn = Integer.parseInt(gRoute.getRouteShortName());
				switch (rsn) {
				// @formatter:off
				case 12: return "University" + _SLASH_ + "Downtown";
				case 23: return "Mayor Magrath" + _SLASH_ + "Scenic"; // Counter Clockwise Loop
				case 24: return "Mayor Magrath" + _SLASH_ + "Scenic"; // Clockwise
				case 31: return "Legacy Rdg" + _SLASH_ + "Uplands";
				case 32: return "Indian Battle"+_SLASH_+ "Columbia Blvd"; // Indian Battle Heights, Varsity Village
				case 33: return "Heritage" + _SLASH_ + "West Highlands" ; // Ridgewood, Heritage, West Highlands
				case 35: return "Copperwood"; // Copperwood
				case 36: return "Sunridge"; // Sunridge, Riverstone, Mtn Hts
				case 37: return "Garry Station"; //
				// @formatter:on
				}
			}
			if ("20N".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return "North Terminal";
			} else if ("20S".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return "South Enmax";
			} else if ("21N".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return "Nord-Bridge";
			} else if ("21S".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return "Henderson Lk" + _SLASH_ + "Industrial";
			} else if ("22N".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return "North Terminal"; // 22 North
			} else if ("22S".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return "South Gate"; //
			}
			throw new MTLog.Fatal("Unexpected route long name for %s!", gRoute);
		}
		return CleanUtils.cleanLabel(routeLongName);
	}

	private static final String AGENCY_COLOR_BLUE_LIGHT = "009ADE"; // BLUE LIGHT (from web site CSS)

	private static final String AGENCY_COLOR = AGENCY_COLOR_BLUE_LIGHT;

	@NotNull
	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Nullable
	@Override
	public String getRouteColor(@NotNull GRoute gRoute) {
		if ("20S".equalsIgnoreCase(gRoute.getRouteShortName())) {
			if ("80FF00".equalsIgnoreCase(gRoute.getRouteColor())) { // too light
				return "81CC2B"; // darker (from PDF schedule)
			}
		} else if ("32".equalsIgnoreCase(gRoute.getRouteShortName())) {
			if ("73CFFF".equalsIgnoreCase(gRoute.getRouteColor())) { // too light
				return "76AFE3"; // darker (from PDF schedule)
			}
		} else if ("36".equalsIgnoreCase(gRoute.getRouteShortName())) {
			if ("80FF80".equalsIgnoreCase(gRoute.getRouteColor())) { // too light
				return "4DB8A4"; // darker (from PDF schedule)
			}
		}
		return super.getRouteColor(gRoute);
	}

	private static final HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;

	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<>();
		//noinspection deprecation
		map2.put(10L, new RouteTripSpec(10L, // splitted because same head-sign in both directions
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "City Ctr", //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University") //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13005"), // University Terminal
								Stops.getALL_STOPS().get("13007"), // ++ UNIVERSITY DR W & VALLEY RD W
								Stops.getALL_STOPS().get("14014") // City Centre Terminal
						)) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("14014"), // City Centre Terminal
								Stops.getALL_STOPS().get("13042"), // ++ COLUMBIA BLVD W & lafayette blvd w
								Stops.getALL_STOPS().get("13005") // University Terminal
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(20L + RID_STARTS_WITH_N, new RouteTripSpec(20L + RID_STARTS_WITH_N, // 20N // splitted because same head-sign in both directions
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "North Terminal", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, EMPTY) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("12216"), // College Terminal
								Stops.getALL_STOPS().get("14014"), // City Centre Terminal
								Stops.getALL_STOPS().get("11053") // North Terminal
						)) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Collections.emptyList()) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(21L + RID_STARTS_WITH_N, new RouteTripSpec(21L + RID_STARTS_WITH_N, // 21N // splitted because no direction
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Westminster", //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "City Ctr") //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("14014"), // City Centre Terminal
								Stops.getALL_STOPS().get("11205") // 19 ST N & 7 AVE N #Westminster
						)) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("11205"), // 19 ST N & 7 AVE N #Westminster
								Stops.getALL_STOPS().get("14015") // City Centre Terminal
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(22L + RID_STARTS_WITH_N, new RouteTripSpec(22L + RID_STARTS_WITH_N, // 22N // splitted because same head-sign in both directions
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "North Terminal", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, EMPTY) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("12106"), // College Terminal
								Stops.getALL_STOPS().get("14015"), // City Centre Terminal
								Stops.getALL_STOPS().get("11210"), // == MEADOWLARK BLVD N & 23 ST N
								Stops.getALL_STOPS().get("11275"), // != 26 AVE N & BLUEFOX BLVD N
								Stops.getALL_STOPS().get("11035"), // != 26 AVE N & ERMINEDALE BLVD N
								Stops.getALL_STOPS().get("11274"), // != 26 AVE N & 23 ST N
								Stops.getALL_STOPS().get("11034"), // != UPLANDS BLVD N & BLUEFOX RD N
								Stops.getALL_STOPS().get("11052") // == North Terminal
						)) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Collections.emptyList()) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(22L + RID_STARTS_WITH_S, new RouteTripSpec(22L + RID_STARTS_WITH_S, // 22S // splitted because same head-sign in both directions
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, EMPTY, //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "College Terminal") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Collections.emptyList()) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("11052"), // North Terminal
								Stops.getALL_STOPS().get("14014"), // City Centre Terminal
								Stops.getALL_STOPS().get("12186"), // == SCENIC DR S & TUDOR CRES S
								Stops.getALL_STOPS().get("12104"), // != COLLEGE DR & 28 AVE S
								Stops.getALL_STOPS().get("12102"), // != Enmax Centre
								Stops.getALL_STOPS().get("12130"), // != Lethbridge Soccer Complex
								Stops.getALL_STOPS().get("12106") // == College Terminal
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(32L, new RouteTripSpec(32L, // splitted because only 1 direction
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University", //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Indian Battle Pk") //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13034"), // RED CROW BLVD W & JERRY POTTS BLVD W
								Stops.getALL_STOPS().get("13039"), // ++
								Stops.getALL_STOPS().get("13068"), // ++
								Stops.getALL_STOPS().get("13005") // University Terminal
						)) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13003"), // University Terminal
								Stops.getALL_STOPS().get("13029"), // ++
								Stops.getALL_STOPS().get("13034") // RED CROW BLVD W & JERRY POTTS BLVD W
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(33L, new RouteTripSpec(33L, // splitted because only 1 direction
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Heritage", // "West Highlands", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13002"), // University Terminal
								Stops.getALL_STOPS().get("13006"), //
								Stops.getALL_STOPS().get("13013"), //
								Stops.getALL_STOPS().get("13020") // HERITAGE BLVD & HERITAGE HEIGHTS PLAZA W
						)) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13020"), // HERITAGE BLVD & HERITAGE HEIGHTS PLAZA W
								Stops.getALL_STOPS().get("13023"), //
								Stops.getALL_STOPS().get("13061"), //
								Stops.getALL_STOPS().get("13002") // University Terminal
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(35L, new RouteTripSpec(35L, // splitted because only 1 direction
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University", //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Crossings") // "Copperwood"
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13311"), // BRITANNIA BLVD W & AQUITANIA BLVD W
								Stops.getALL_STOPS().get("13043"), // ++
								Stops.getALL_STOPS().get("13004") // University Terminal
						)) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13004"), // University Terminal
								Stops.getALL_STOPS().get("13303"), // ++
								Stops.getALL_STOPS().get("13311") // BRITANNIA BLVD W & AQUITANIA BLVD W
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(36L, new RouteTripSpec(36L, // splitted because only 1 direction
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Sunridge") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13507"), // MT SUNDANCE RD W & MT SUNDIAL CRT W
								Stops.getALL_STOPS().get("13517"), // ++
								Stops.getALL_STOPS().get("13001") // University Terminal
						)) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13001"), // University Terminal
								Stops.getALL_STOPS().get("13503"), // ++
								Stops.getALL_STOPS().get("13507") // MT SUNDANCE RD W & MT SUNDIAL CRT W
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(37L, new RouteTripSpec(37L, // splitted because only 1 direction
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Garry Sta") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13524"), // Garry Station Prt W & Garry Dr W
								Stops.getALL_STOPS().get("13450"), // GARRY DR W & SQUAMISH BLVD W
								Stops.getALL_STOPS().get("13000") // University Terminal
						)) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList( //
								Stops.getALL_STOPS().get("13000"), // University Terminal
								Stops.getALL_STOPS().get("13009"), // University Dr W & Edgewood Blvd W
								Stops.getALL_STOPS().get("13524") // Garry Station Prt W & Garry Dr W
						)) //
				.compileBothTripSort());
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, @NotNull List<MTripStop> list1, @NotNull List<MTripStop> list2, @NotNull MTripStop ts1, @NotNull MTripStop ts2, @NotNull GStop ts1GStop, @NotNull GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop, this);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@NotNull
	@Override
	public ArrayList<MTrip> splitTrip(@NotNull MRoute mRoute, @Nullable GTrip gTrip, @NotNull GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@NotNull
	@Override
	public Pair<Long[], Integer[]> splitTripStop(@NotNull MRoute mRoute, @NotNull GTrip gTrip, @NotNull GTripStop gTripStop, @NotNull ArrayList<MTrip> splitTrips, @NotNull GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()), this);
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	@Override
	public void setTripHeadsign(@NotNull MRoute mRoute, @NotNull MTrip mTrip, @NotNull GTrip gTrip, @NotNull GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		String tripHeadsign = gTrip.getTripHeadsignOrDefault();
		if (tripHeadsign.isEmpty()) {
			tripHeadsign = mRoute.getLongName();
		}
		mTrip.setHeadsignString(
				cleanTripHeadsign(tripHeadsign),
				gTrip.getDirectionIdOrDefault()
		);
	}

	@Override
	public boolean directionFinderEnabled() {
		return false; // DISABLED because provided direction_id useless & 2 routes = 1 route w/ 2 directions
	}

	@Override
	public boolean mergeHeadsign(@NotNull MTrip mTrip, @NotNull MTrip mTripToMerge) {
		throw new MTLog.Fatal("Unexpected trips to merge %s & %s!", mTrip, mTripToMerge);
	}

	private static final String UNIVERSITY_OF_SHORT = "U of";
	private static final Pattern UNIVERSITY_OF = Pattern.compile("((^|\\W)(university of)(\\W|$))", Pattern.CASE_INSENSITIVE);
	private static final String UNIVERSITY_OF_REPLACEMENT = "$2" + UNIVERSITY_OF_SHORT + "$4";

	private static final Pattern ENDS_WITH_LOOP = Pattern.compile("([\\s]*loop$)", Pattern.CASE_INSENSITIVE);
	private static final Pattern ENDS_WITH_ROUTE = Pattern.compile("([\\s]*route$)", Pattern.CASE_INSENSITIVE);

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = ENDS_WITH_LOOP.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = ENDS_WITH_ROUTE.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = UNIVERSITY_OF.matcher(tripHeadsign).replaceAll(UNIVERSITY_OF_REPLACEMENT);
		tripHeadsign = CleanUtils.fixMcXCase(tripHeadsign);
		tripHeadsign = CleanUtils.CLEAN_AT.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AND.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanBounds(tripHeadsign);
		tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	private String[] getIgnoredWords() {
		return new String[]{"ATB"};
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.toLowerCaseUpperCaseWords(Locale.ENGLISH, gStopName, getIgnoredWords());
		gStopName = CleanUtils.fixMcXCase(gStopName);
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanBounds(gStopName);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	@Override
	public int getStopId(@NotNull GStop gStop) {
		if (Utils.isDigitsOnly(gStop.getStopCode())) {
			return Integer.parseInt(gStop.getStopCode()); // use stop code as stop ID
		}
		Matcher matcher = DIGITS.matcher(gStop.getStopCode());
		if (matcher.find()) {
			int digits = Integer.parseInt(matcher.group());
			if (gStop.getStopCode().startsWith("MESC")) {
				return 13_050_000 + digits;
			}
		}
		throw new MTLog.Fatal("Unexpected stop ID for %s!", gStop);
	}
}
