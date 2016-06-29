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

	private static final String STOP_11006 = "8f8cb4e8-1419-435a-9257-32ac60b3fb02";
	private static final String STOP_11023 = "8571441a-730e-4448-8864-f7dded9ee6d6";
	private static final String STOP_11052 = "b67dc4a8-25bc-4cbd-8044-02f02f307de7";
	private static final String STOP_11054 = "5c4f488b-e5a8-4677-b5b8-59a164ce8b8c";
	private static final String STOP_11141 = "b83dee2b-34be-49a3-88f8-56e25eaf70fc";
	private static final String STOP_11143 = "71f11896-7f1b-4f85-a782-4c2cdefca765";
	private static final String STOP_11208 = "100f6f4c-1740-4109-bed4-fac770016af1";
	private static final String STOP_11262 = "0bd46508-4c56-4c4b-9151-e1ce73f4ab88";
	private static final String STOP_11271 = "73f99553-6fde-41db-9d63-c633fc4e1ba9";
	private static final String STOP_12009 = "060b3951-10d5-40ae-b1a0-9bb7383a0f30";
	private static final String STOP_12010 = "aacff3d5-cedc-4da8-96ec-c79f36a2166d";
	private static final String STOP_12011 = "7400b4b8-6d55-46ee-9694-5acbcc292c0c";
	private static final String STOP_12020 = "a2058662-a49b-453d-a5ed-de18e5b5f1aa";
	private static final String STOP_12034 = "82ddf3b1-c120-4ffc-b221-674ecc6be33d";
	private static final String STOP_12035 = "ab13b9e1-07f0-43d9-8da0-c44d30a44f7a";
	private static final String STOP_12038 = "00f07baf-c80d-4997-8f79-025280c06ce1";
	private static final String STOP_12330 = "9e8bf681-a0b9-417a-bba7-8a2530c94ef7";
	private static final String STOP_13001 = "93e46ba5-627d-4fc1-a87e-14540af091bf";
	private static final String STOP_13002 = "4598db1b-0273-45de-b8cc-d3ae2626a73a";
	private static final String STOP_13004 = "f70c616a-b669-4cc2-b123-722d1d175042";
	private static final String STOP_13005 = "2fbf4d2e-9d60-4ef9-bf89-d2e1090c9b8d";
	private static final String STOP_13013 = "8205b720-d462-42ca-81cd-efc3f0c07e66";
	private static final String STOP_13020 = "1267f46e-d6ee-4f86-9d17-7b5ea69861b3";
	private static final String STOP_13023 = "7aaf4c66-2c54-4f05-b333-d749bfe7d394";
	private static final String STOP_13029 = "e7442b80-3068-43ff-a606-5d0fa08a3c57";
	private static final String STOP_13034 = "5d20b41e-5605-42c0-86b2-852d4c50e30b";
	private static final String STOP_13039 = "7fe35d39-2b4b-48bf-a042-2d11eb3ffdba";
	private static final String STOP_13043 = "ae7fa6b3-9451-46d9-a42f-c63b8dbbecf4";
	private static final String STOP_13068 = "48777031-b28e-4cc8-b4e8-cddb29cff3c8";
	private static final String STOP_13303 = "345aa195-461d-4be6-8011-c9c9a9d02f64";
	private static final String STOP_13311 = "925437ad-5c27-40b7-a7f8-ac1dc6254f85";
	private static final String STOP_13503 = "53eed5e7-2dd8-4ca5-9c99-97816fdf9aba";
	private static final String STOP_13507 = "1330661e-b50b-4bc5-997f-684767c4a7ae";
	private static final String STOP_13517 = "6cc3977a-c79d-49b1-a653-ee2b9219c200";
	private static final String STOP_14014 = "6c06783c-2b7f-4113-a039-48c3a24ae773";
	private static final String STOP_14016 = "012cf9c1-9102-488e-8858-3131bc98db0d";
	private static final String STOP_14034 = "19e7f2fb-445c-474f-a9d9-54ea0393aac9";
	private static final String STOP_MESC1 = "d427ab43-1007-4182-b7ba-a8f81cbcddd5";
	private static final String STOP_MESC2 = "b15bf429-37b6-42f2-8faf-6e0807cc639d";
	// private static final String STOP_MESC3 = "64b8a95f-e8f7-44b1-85db-3946744cbd16";
	private static final String STOP_MESC4 = "eab61b92-af2d-4c07-86a4-306b89164fff";

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
						STOP_14016, // ==
								STOP_14034, // !=
								STOP_12038, // ==
								STOP_12020, //
								STOP_12011, // !=
								STOP_12010, // ==
								STOP_12330, // !=
								STOP_MESC1, //
								STOP_11271 //
						})) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						STOP_11271, //
								STOP_11054, // !=
								STOP_12010, // ==
								STOP_12009, // !=
								STOP_12034, //
								// STOP_MESC1, //
								STOP_MESC2, //
								STOP_MESC4, //
								STOP_12035, //
								STOP_14014 //
						})) //
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
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, HERITAGE, // "West Highlands", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY) //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { STOP_13002, "92d5a30e-ceee-4eea-8d3f-5fab36f9ec42", STOP_13013, STOP_13020 })) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { STOP_13020, STOP_13023, "0819bac5-24ce-44b7-b870-7493f18193d3", STOP_13002 })) //
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
