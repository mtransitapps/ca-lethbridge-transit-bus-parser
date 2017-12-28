package org.mtransit.parser.ca_lethbridge_transit_bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Pair;
import org.mtransit.parser.SplitUtils;
import org.mtransit.parser.SplitUtils.RouteTripSpec;
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

// http://opendata.lethbridge.ca/
// http://opendata.lethbridge.ca/datasets?keyword=GroupTransportation
// http://opendata.lethbridge.ca/datasets?keyword=GroupGTFSTransit
// http://www.lethbridge.ca/OpenDataSets/GTFS_Transit_Data.zip
public class LethbridgeTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/ca-lethbridge-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new LethbridgeTransitBusAgencyTools().start(args);
	}

	private HashSet<String> serviceIds;

	@Override
	public void start(String[] args) {
		System.out.printf("\nGenerating Lethbridge Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this);
		super.start(args);
		System.out.printf("\nGenerating Lethbridge Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludeCalendar(GCalendar gCalendar) {
		if (this.serviceIds != null) {
			return excludeUselessCalendar(gCalendar, this.serviceIds);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(GCalendarDate gCalendarDates) {
		if (this.serviceIds != null) {
			return excludeUselessCalendarDate(gCalendarDates, this.serviceIds);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(GTrip gTrip) {
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public boolean excludeRoute(GRoute gRoute) {
		return super.excludeRoute(gRoute);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	private static final Pattern DIGITS = Pattern.compile("[\\d]+");

	private static final String N = "n";
	private static final String S = "s";

	private static final long RID_STARTS_WITH_N = 140000l;
	private static final long RID_STARTS_WITH_S = 190000l;

	@Override
	public long getRouteId(GRoute gRoute) {
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
		System.out.printf("\nUnexpected route ID for %s!\n", gRoute);
		System.exit(-1);
		return -1l;
	}

	private static final String SLASH = " / ";

	private static final String CITY_CTR = "City Ctr";
	private static final String COLUMBIA_BLVD = "Columbia Blvd";
	private static final String COPPERWOOD = "Copperwood";
	private static final String CROSSINGS = "Crossings";
	private static final String DOWNTOWN = "Downtown";
	private static final String HENDERSON_LK = "Henderson Lk";
	private static final String HERITAGE = "Heritage";
	private static final String INDIAN_BATTLE = "Indian Battle";
	private static final String INDUSTRIAL = "Industrial";
	private static final String LEGACY_RDG = "Legacy Rdg";
	private static final String MAYOR_MAGRATH = "Mayor Magrath";
	private static final String NORD_BRIDGE = "Nord-Bridge";
	private static final String NORTH_TERMINAL = "North Terminal";
	private static final String SCENIC = "Scenic";
	private static final String SOUTH_ENMAX = "South Enmax";
	private static final String SOUTH_GATE = "South Gate";
	private static final String SUNRIDGE = "Sunridge";
	private static final String UNIVERSITY = "University";
	private static final String UPLANDS = "Uplands";
	private static final String WEST_HIGHLANDS = "West Highlands";
	//
	private static final String HENDERSON_LK_INDUSTRIAL = HENDERSON_LK + SLASH + INDUSTRIAL;
	private static final String HERITAGE_WEST_HIGHLANDS = HERITAGE + SLASH + WEST_HIGHLANDS;
	private static final String INDIAN_BATTLE_COLUMBIA_BLVD = INDIAN_BATTLE + SLASH + COLUMBIA_BLVD;
	private static final String LEGACY_RDG_UPLANDS = LEGACY_RDG + SLASH + UPLANDS;
	private static final String MAYOR_MAGRATH_SCENIC = MAYOR_MAGRATH + SLASH + SCENIC;
	private static final String UNIVERSITY_DOWNTOWN = UNIVERSITY + SLASH + DOWNTOWN;

	@Override
	public String getRouteLongName(GRoute gRoute) {
		String routeLongName = gRoute.getRouteLongName();
		if (StringUtils.isEmpty(routeLongName)) {
			routeLongName = gRoute.getRouteDesc(); // using route description as route long name
		}
		if (StringUtils.isEmpty(routeLongName)) {
			if (Utils.isDigitsOnly(gRoute.getRouteShortName())) {
				int rsn = Integer.parseInt(gRoute.getRouteShortName());
				switch (rsn) {
				// @formatter:off
				case 12: return UNIVERSITY_DOWNTOWN;
				case 23: return MAYOR_MAGRATH_SCENIC; // Counter Clockwise Loop
				case 24: return MAYOR_MAGRATH_SCENIC; // Clockwise
				case 31: return LEGACY_RDG_UPLANDS;
				case 32: return INDIAN_BATTLE_COLUMBIA_BLVD; // Indian Battle Heights, Varsity Village
				case 33: return HERITAGE_WEST_HIGHLANDS; // Ridgewood, Heritage, West Highlands
				case 35: return COPPERWOOD; // Copperwood
				case 36: return SUNRIDGE; // Sunridge, Riverstone, Mtn Hts
				case 37: return "Garry Station"; //
				// @formatter:on
				}
			}
			if ("20N".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return NORTH_TERMINAL;
			} else if ("20S".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return SOUTH_ENMAX;
			} else if ("21N".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return NORD_BRIDGE;
			} else if ("21S".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return HENDERSON_LK_INDUSTRIAL;
			} else if ("22N".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return NORTH_TERMINAL; // 22 North
			} else if ("22S".equalsIgnoreCase(gRoute.getRouteShortName())) {
				return SOUTH_GATE; //
			}
			System.out.printf("\nUnexpected route long name for %s!\n", gRoute);
			System.exit(-1);
			return null;
		}
		return CleanUtils.cleanLabel(routeLongName);
	}

	private static final String AGENCY_COLOR_BLUE_LIGHT = "009ADE"; // BLUE LIGHT (from web site CSS)

	private static final String AGENCY_COLOR = AGENCY_COLOR_BLUE_LIGHT;

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	@Override
	public String getRouteColor(GRoute gRoute) {
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

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;
	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<Long, RouteTripSpec>();
		map2.put(21l + RID_STARTS_WITH_N, new RouteTripSpec(21l + RID_STARTS_WITH_N, // 21N
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, NORD_BRIDGE, //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, CITY_CTR) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("14014"), //
								Stops.ALL_STOPS.get("11143"), //
								Stops.ALL_STOPS.get("11208") //
						})) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("11208"), //
								Stops.ALL_STOPS.get("11141"), //
								Stops.ALL_STOPS.get("14016"), //
						})) //
				.compileBothTripSort());
		map2.put(21l + RID_STARTS_WITH_S, new RouteTripSpec(21l + RID_STARTS_WITH_S, // 21S
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, HENDERSON_LK_INDUSTRIAL, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, CITY_CTR) //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("14016"), // == City Center Terminal
								Stops.ALL_STOPS.get("14034"), // !=
								Stops.ALL_STOPS.get("12038"), // ==
								Stops.ALL_STOPS.get("12011"), // !=
								Stops.ALL_STOPS.get("12010"), // == Transfer Point
								Stops.ALL_STOPS.get("12330"), // !=
								Stops.ALL_STOPS.get("11267"), // !=
								Stops.ALL_STOPS.get("11271"), // 14 AVE N & 39 ST N
						})) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("11271"), // 14 AVE N & 39 ST N
								Stops.ALL_STOPS.get("11268"), // !=
								Stops.ALL_STOPS.get("11054"), // !=
								Stops.ALL_STOPS.get("12010"), // == Transfer Point
								Stops.ALL_STOPS.get("12009"), // !=
								Stops.ALL_STOPS.get("12034"), // ==
								Stops.ALL_STOPS.get("MESC2"), // !=
								Stops.ALL_STOPS.get("MESC3"), // !=
								Stops.ALL_STOPS.get("12035"), // ==
								Stops.ALL_STOPS.get("14014"), // City Center Terminal
						})) //
				.compileBothTripSort());
		map2.put(23L, new RouteTripSpec(23L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "CCW", //
				1, MTrip.HEADSIGN_TYPE_STRING, "") //
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("14016"), // City Centre Terminal
								Stops.ALL_STOPS.get("14017"), // ++ 5 AVE & 4 ST S
								Stops.ALL_STOPS.get("12104"), // ++ COLLEGE DR & 28 AVE S
								Stops.ALL_STOPS.get("12217"), // Lethbridge College
								Stops.ALL_STOPS.get("12131"), // ++ 28 AVE S & 28 ST S
								Stops.ALL_STOPS.get("12203"), // ++ MAYOR MAGRATH DR S & SOUTH PARKSIDE DR S
								Stops.ALL_STOPS.get("11035"), // ++ 26 AVE N & ERMINEDALE BLVD N
								Stops.ALL_STOPS.get("11052"), // North Terminal
								Stops.ALL_STOPS.get("11112"), // ++ STAFFORD DR N & STAFFORD RD N
								Stops.ALL_STOPS.get("14013"), // ++ 4 AVE S & 3 ST S
								Stops.ALL_STOPS.get("14016"), // City Centre Terminal
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						/* no stops */
						})) //
				.compileBothTripSort());
		map2.put(24L, new RouteTripSpec(24L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "CW", //
				1, MTrip.HEADSIGN_TYPE_STRING, "") //
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("14021"), // City Centre Terminal
								Stops.ALL_STOPS.get("11052"), // North Terminal
								Stops.ALL_STOPS.get("12217"), // Lethbridge College
								Stops.ALL_STOPS.get("14021"), // City Centre Terminal
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						/* no stops */
						})) //
				.compileBothTripSort());
		map2.put(31l, new RouteTripSpec(31l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, NORTH_TERMINAL, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, INDUSTRIAL) //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("11262"), //
								Stops.ALL_STOPS.get("11023"), //
								Stops.ALL_STOPS.get("11052"), // North Terminal
						})) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("11052"), // North Terminal
								Stops.ALL_STOPS.get("11006"), //
								Stops.ALL_STOPS.get("11262"), //
						})) //
				.compileBothTripSort());
		map2.put(32l, new RouteTripSpec(32l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, INDIAN_BATTLE) //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13034"), //
								Stops.ALL_STOPS.get("13039"), //
								Stops.ALL_STOPS.get("13068"), //
								Stops.ALL_STOPS.get("13005"), //
						})) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13001"), //
								Stops.ALL_STOPS.get("13029"), //
								Stops.ALL_STOPS.get("13034"), //
						})) //
				.compileBothTripSort());
		map2.put(33l, new RouteTripSpec(33l, //
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, HERITAGE, // "West Highlands", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13002"), //
								Stops.ALL_STOPS.get("13006"), //
								Stops.ALL_STOPS.get("13013"), //
								Stops.ALL_STOPS.get("13020"), //
						})) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13020"), //
								Stops.ALL_STOPS.get("13023"), //
								Stops.ALL_STOPS.get("13061"), //
								Stops.ALL_STOPS.get("13002"), //
						})) //
				.compileBothTripSort());
		map2.put(35l, new RouteTripSpec(35l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, CROSSINGS) // "Copperwood"
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13311"), //
								Stops.ALL_STOPS.get("13043"), //
								Stops.ALL_STOPS.get("13004"), //
						})) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13004"), //
								Stops.ALL_STOPS.get("13303"), //
								Stops.ALL_STOPS.get("13311"), //
						})) //
				.compileBothTripSort());
		map2.put(36l, new RouteTripSpec(36l, //
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, SUNRIDGE) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13507"), //
								Stops.ALL_STOPS.get("13517"), //
								Stops.ALL_STOPS.get("13004"), // University Terminal
						})) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13004"), // University Terminal
								Stops.ALL_STOPS.get("13503"), //
								Stops.ALL_STOPS.get("13507"), //
						})) //
				.compileBothTripSort());
		map2.put(37l, new RouteTripSpec(37l, //
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Garry Sta") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13524"), // Garry Station Prt W & Garry Dr W
								Stops.ALL_STOPS.get("13450"), // GARRY DR W & SQUAMISH BLVD W
								Stops.ALL_STOPS.get("13004"), // University Terminal
						})) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						Stops.ALL_STOPS.get("13004"), // University Terminal
								Stops.ALL_STOPS.get("13009"), // University Dr W & Edgewood Blvd W
								Stops.ALL_STOPS.get("13524"), // Garry Station Prt W & Garry Dr W
						})) //
				.compileBothTripSort());
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, List<MTripStop> list1, List<MTripStop> list2, MTripStop ts1, MTripStop ts2, GStop ts1GStop, GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop, this);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@Override
	public ArrayList<MTrip> splitTrip(MRoute mRoute, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@Override
	public Pair<Long[], Integer[]> splitTripStop(MRoute mRoute, GTrip gTrip, GTripStop gTripStop, ArrayList<MTrip> splitTrips, GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()), this);
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		String tripHeadsign = gTrip.getTripHeadsign();
		mTrip.setHeadsignString(cleanTripHeadsign(tripHeadsign), gTrip.getDirectionId());
	}

	private static final String UNIVERSITY_OF_SHORT = "U of";
	private static final Pattern UNIVERSITY_OF = Pattern.compile("((^|\\W){1}(university of)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String UNIVERSITY_OF_REPLACEMENT = "$2" + UNIVERSITY_OF_SHORT + "$4";

	private static final Pattern ENDS_WITH_LOOP = Pattern.compile("([\\s]*loop$)", Pattern.CASE_INSENSITIVE);
	private static final Pattern ENDS_WITH_ROUTE = Pattern.compile("([\\s]*route$)", Pattern.CASE_INSENSITIVE);

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		tripHeadsign = ENDS_WITH_LOOP.matcher(tripHeadsign).replaceAll(StringUtils.EMPTY);
		tripHeadsign = ENDS_WITH_ROUTE.matcher(tripHeadsign).replaceAll(StringUtils.EMPTY);
		tripHeadsign = UNIVERSITY_OF.matcher(tripHeadsign).replaceAll(UNIVERSITY_OF_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AT.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		tripHeadsign = CleanUtils.CLEAN_AND.matcher(tripHeadsign).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@Override
	public String cleanStopName(String gStopName) {
		if (Utils.isUppercaseOnly(gStopName, true, true)) {
			gStopName = gStopName.toLowerCase(Locale.ENGLISH);
		}
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.removePoints(gStopName);
		gStopName = CleanUtils.cleanNumbers(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	@Override
	public int getStopId(GStop gStop) {
		if (Utils.isDigitsOnly(gStop.getStopCode())) {
			return Integer.parseInt(gStop.getStopCode()); // use stop code as stop ID
		}
		Matcher matcher = DIGITS.matcher(gStop.getStopCode());
		if (matcher.find()) {
			int digits = Integer.parseInt(matcher.group());
			if (gStop.getStopCode().startsWith("MESC")) {
				return 13050000 + digits;
			}
		}
		System.out.printf("\nUnexpected stop ID for %s!\n", gStop);
		System.exit(-1);
		return -1;
	}
}
