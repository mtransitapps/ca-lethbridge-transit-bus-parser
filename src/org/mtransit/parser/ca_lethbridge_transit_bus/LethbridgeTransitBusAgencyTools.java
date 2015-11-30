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

	private static final String STOP_11006 = "796fbcd7-24bc-447a-9243-bea70d43b448";
	private static final String STOP_11023 = "68e13543-e1a1-4dc5-b8c6-e3c4bdd5b016";
	private static final String STOP_11052 = "ccb8ac08-e605-4178-a052-6590a15d58a1";
	private static final String STOP_11054 = "10ec13b7-f29a-4ab4-8cf4-79c34abf5d37";
	private static final String STOP_11141 = "d37be4b6-e646-4e03-b8f6-6e8477183a63";
	private static final String STOP_11143 = "69986838-09a5-4167-b7f6-d67cf768184c";
	private static final String STOP_11208 = "e4c54018-66ff-47c7-8a81-63bd0ab42baa";
	private static final String STOP_11262 = "bdf87713-f306-43ce-8268-bcc5d3abfe32";
	private static final String STOP_11271 = "32c07ca4-63f5-4d44-9116-32822d0cf27a";
	private static final String STOP_12009 = "c537c902-6d90-4d33-a013-5b3b9dd4ae15";
	private static final String STOP_12010 = "0e2a77b0-229e-448d-863f-8e87a20c7b3e";
	private static final String STOP_12011 = "c444f08d-7791-40e3-80ec-e38722e9c217";
	private static final String STOP_12020 = "795d3f8f-93b6-470e-aa6e-a4da0a02b610";
	private static final String STOP_12034 = "b13463a1-7979-4f76-b59b-1410b08be34d";
	private static final String STOP_12035 = "6297040b-5f4a-4817-81dd-20d2b6eb2586";
	private static final String STOP_12038 = "694b3284-4441-4eae-ac44-9ac21d5b736a";
	private static final String STOP_12330 = "abd089a4-6a47-44ab-9482-11fe40f97564";
	private static final String STOP_13001 = "79925710-4213-44c0-b7ca-9e82a52f0f4a";
	private static final String STOP_13002 = "bb8c5d83-c81d-45d0-ac32-e8f7d7386a0a";
	private static final String STOP_13004 = "7b883655-ce1c-4fbc-a139-1b1c1437db68";
	private static final String STOP_13005 = "39824b48-7928-4c61-b350-8ecc3a599268";
	private static final String STOP_13013 = "f94305d8-021b-4e46-8453-dceb49f495d1";
	private static final String STOP_13020 = "d2731589-0d40-4796-8ea7-5b398d1482d9";
	private static final String STOP_13023 = "676ddba7-26cb-4bae-9315-dfe27446baa8";
	private static final String STOP_13029 = "387079b4-eabb-45aa-9a0c-a9407f661f1f";
	private static final String STOP_13034 = "27799ebf-7335-4601-954d-71203210bf1b";
	private static final String STOP_13039 = "89b5eb3b-cee1-4434-a79a-1dc2102fe95b";
	private static final String STOP_13043 = "e6ae3828-2197-49da-a0e7-3aee3d82667a";
	private static final String STOP_13068 = "4f844c1c-e4f8-40d4-bd86-afb638e1a0dc";
	private static final String STOP_13303 = "d7b57888-64f0-4c84-b272-20442112841f";
	private static final String STOP_13311 = "98ce590c-1a59-4672-a5ff-b4728e7e3803";
	private static final String STOP_13503 = "60af58cd-e4f6-47b7-845e-67f8e0b83dcf";
	private static final String STOP_13507 = "5866f8d4-f536-4f69-b83f-b5b12255716a";
	private static final String STOP_13517 = "c51e057d-0175-4c58-b607-3bf5a634a58c";
	private static final String STOP_14014 = "3f7bca86-02cd-4ec3-b223-498b43cbfa69";
	private static final String STOP_14016 = "b6876f1c-6381-4225-8049-1254bd076e4b";
	private static final String STOP_14034 = "d7e6e65a-0d88-4b10-bd0e-cf27b181429d";
	private static final String STOP_MESC1 = "cfe84a7e-a843-49c8-a2fd-79c7b024e1ab";
	private static final String STOP_MESC2 = "ab831f42-8885-4923-8aa1-5393ab66177c";
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
