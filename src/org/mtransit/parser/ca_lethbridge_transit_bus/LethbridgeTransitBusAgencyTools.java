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
	//
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
				case 24: return MAYOR_MAGRATH_SCENIC; // Clockwise
				case 31: return LEGACY_RDG_UPLANDS;
				case 32: return INDIAN_BATTLE_COLUMBIA_BLVD; // Indian Battle Heights, Varsity Village
				case 33: return HERITAGE_WEST_HIGHLANDS; // Ridgewood, Heritage, West Highlands
				case 35: return COPPERWOOD; // Copperwood
				case 36: return SUNRIDGE; // Sunridge, Riverstone, Mtn Hts
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

	private static final String STOP_11006 = "c29d7f36-9cdd-4b99-a51e-ec94e414bf3b";
	private static final String STOP_11023 = "c9c28b82-b1a7-49d6-aacc-a18eea5df686";
	private static final String STOP_11052 = "72ff623a-de78-4314-b382-77bc3fac801c";
	private static final String STOP_11054 = "bd34718d-d1ea-41ec-a85a-5e7cc2790252";
	private static final String STOP_11141 = "3f1aa0dd-6bd0-471a-8bc8-b417ab917331";
	private static final String STOP_11143 = "71f11896-7f1b-4f85-a782-4c2cdefca765";
	private static final String STOP_11208 = "ec32169e-0999-464b-a23d-3e0a4944142c";
	private static final String STOP_11262 = "86adc8ca-b457-4ea5-93c8-63a064506105";
	private static final String STOP_11271 = "8752cdef-659a-46fc-9cfd-e985db92069e";
	private static final String STOP_12009 = "2c1d9072-fbe6-439d-b743-fff26c24c393";
	private static final String STOP_12010 = "ecc51f95-ca6d-42cf-b90d-8d16b31b3347";
	private static final String STOP_12011 = "571269cf-d029-4bfe-97aa-a28bc5715116";
	private static final String STOP_12020 = "9ce70779-2322-4f87-a28f-786751dd2207";
	private static final String STOP_12034 = "688cb81a-7b4b-4335-8af8-dafb0bb05d7e";
	private static final String STOP_12035 = "b2d6ab69-0aae-4647-90b2-bd5348f1299f";
	private static final String STOP_12038 = "d4d680ee-1388-432c-8957-e5ab57f22f90";
	private static final String STOP_12330 = "3bc6803a-9f5b-4049-8ebc-6cb345d412af";
	private static final String STOP_13001 = "e8ed3065-2a31-4cce-8986-b07e3852799a";
	private static final String STOP_13002 = "3f84cdce-cb26-439c-bda0-b647019bd0c6";
	private static final String STOP_13004 = "e4b893f6-ab4a-4106-b9b4-b8d8d4936c1a";
	private static final String STOP_13005 = "e03cfee3-b0fd-4077-95a8-ad2c475cd7d2";
	private static final String STOP_13013 = "b7ddf5bc-21d0-4c12-a6d1-06a034864310";
	private static final String STOP_13020 = "ea834c53-7ec9-43b3-8274-9dc799eb49d1";
	private static final String STOP_13023 = "52fef153-8e21-4484-a775-8191864286ee";
	private static final String STOP_13029 = "ce8771a7-4f53-4564-a44b-274f0cb33bb1";
	private static final String STOP_13034 = "9b9e7bd8-fd5d-46ed-b805-d40f38ab1c2c";
	private static final String STOP_13039 = "92cf785d-26eb-4fd7-97ad-a4be6a5941a1";
	private static final String STOP_13043 = "1b4ef03e-1058-4f2f-aaff-3061f85428b9";
	private static final String STOP_13068 = "563f7a32-df9f-4d43-9a0a-ac3a2965266f";
	private static final String STOP_13303 = "7730cd28-cfe5-4d1f-bfb2-fd53e4d6a099";
	private static final String STOP_13311 = "dda15d0e-c704-412f-ba88-d746d0f65891";
	private static final String STOP_13503 = "3cd696e3-31b6-4fff-9e35-c0d94e6ca988";
	private static final String STOP_13507 = "1e0149ee-f5b8-454b-871a-56fa1f73e411";
	private static final String STOP_13517 = "c8dd4989-8665-4832-bef3-be662fece6c0";
	private static final String STOP_14014 = "c0e36ad0-b668-472c-ba4c-27195c9bc8f0";
	private static final String STOP_14016 = "03cd648a-156e-4a49-a7d3-d6fd17604ad5";
	private static final String STOP_14034 = "68851b86-14db-4571-8e42-4158eb2db6a2";
	private static final String STOP_MESC1 = "81fad63a-7df9-478f-9f30-552220298430";
	private static final String STOP_MESC2 = "a5af132d-e7c7-44af-a9c1-8fc220dfa9c2";

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;
	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<Long, RouteTripSpec>();
		map2.put(21l + RID_STARTS_WITH_N, new RouteTripSpec(21l + RID_STARTS_WITH_N, // 21N
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, NORD_BRIDGE, //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, CITY_CTR) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { STOP_14014, STOP_11143, STOP_11208 })) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { STOP_11208, STOP_11141, STOP_14016 })) //
				.compileBothTripSort());
		map2.put(21l + RID_STARTS_WITH_S, new RouteTripSpec(21l + RID_STARTS_WITH_S, // 21S
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, HENDERSON_LK_INDUSTRIAL, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, CITY_CTR) //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						STOP_14016, STOP_14034, STOP_12038, STOP_12020, STOP_12011, STOP_12010, STOP_12330, STOP_11271 })) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						STOP_11271, STOP_11054, STOP_12010, STOP_12009, STOP_12034, STOP_MESC1, STOP_MESC2, STOP_12035, STOP_14014 })) //
				.compileBothTripSort());
		map2.put(31l, new RouteTripSpec(31l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, NORTH_TERMINAL, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, INDUSTRIAL) //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { STOP_11262, STOP_11023, STOP_11052 })) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { STOP_11052, STOP_11006, STOP_11262 })) //
				.compileBothTripSort());
		map2.put(32l, new RouteTripSpec(32l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, INDIAN_BATTLE) //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { STOP_13034, STOP_13039, STOP_13068, STOP_13005 })) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { STOP_13001, STOP_13029, STOP_13034 })) //
				.compileBothTripSort());
		map2.put(33l, new RouteTripSpec(33l, //
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, HERITAGE, // "West Highlands"
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { STOP_13002, STOP_13013, STOP_13020 })) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { STOP_13020, STOP_13023, STOP_13002 })) //
				.compileBothTripSort());
		map2.put(35l, new RouteTripSpec(35l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, CROSSINGS) // "Copperwood"
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { STOP_13311, STOP_13043, STOP_13004 })) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { STOP_13004, STOP_13303, STOP_13311 })) //
				.compileBothTripSort());
		map2.put(36l, new RouteTripSpec(36l, //
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, SUNRIDGE) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { STOP_13507, STOP_13517, STOP_13004 })) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { STOP_13004, STOP_13503, STOP_13507 })) //
				.compileBothTripSort());
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, List<MTripStop> list1, List<MTripStop> list2, MTripStop ts1, MTripStop ts2, GStop ts1GStop, GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
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
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()));
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	private static final String CLOCKWISE = "Clockwise";
	private static final String COUNTER_CLOCKWISE = "Counter Clockwise";

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		String tripHeadsign = gTrip.getTripHeadsign();
		if (StringUtils.isEmpty(tripHeadsign)) {
			if (mRoute.getId() == 23l) {
				if (gTrip.getDirectionId() == 0) {
					tripHeadsign = COUNTER_CLOCKWISE;
				}
			} else if (mRoute.getId() == 24l) {
				if (gTrip.getDirectionId() == 0) {
					tripHeadsign = CLOCKWISE;
				}
			}
		}
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
