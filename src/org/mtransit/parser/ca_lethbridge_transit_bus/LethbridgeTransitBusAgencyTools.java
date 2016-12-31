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

	private static final String STOP_11006 = "3b1041d9-46c8-4c3e-b0b5-8f2973e7067f"; // 13 ST N & 41 AVE N
	private static final String STOP_11023 = "4823a099-6de2-4013-9a2e-4f4c6be6a3b8"; // KODIAK BLVD N & KODIAK PL N
	private static final String STOP_11052 = "4091700d-2975-4e28-ba42-76b099c7ed45"; // North Terminal
	private static final String STOP_11054 = "29c3f451-918a-407e-8fb0-156aa3ac00fe"; // 36 ST S & CROWSNEST HWY
	private static final String STOP_11141 = "990ff628-2d5c-4e26-b31f-bc3ea7303682"; // 13 ST N & 4 AVE N
	private static final String STOP_11143 = "bd1f2a3c-18c7-4560-b491-d65f14d1ab08"; // 13 ST N & 2 AVE N
	private static final String STOP_11208 = "c6d427eb-9174-49a1-9d93-aeb5fbd4152b"; // 12 AVE N & 19 ST N
	private static final String STOP_11262 = "417cadb5-0e4a-4d8e-a441-029b8097af7e"; // GIFFEN RD N & 36 ST N
	private static final String STOP_11267 = "808a0a9f-9fb9-44d6-9055-620527f1e115"; // 39 ST N & 14 AVE N
	private static final String STOP_11268 = "2c6e09bb-0f6f-47ff-aad4-2aaee574751d"; // 36 ST N & 14 AVE N
	private static final String STOP_11271 = "eb1b46ef-b948-4014-9c90-3ad320ebe69d"; // 14 AVE N & 39 ST N
	private static final String STOP_12009 = "89476510-88dc-4eee-989a-675301f90f6b"; // 28 ST S & 3 AVE S
	private static final String STOP_12010 = "6b3e17f5-468f-4db6-bb20-205a4b25522a"; // Transfer Point
	private static final String STOP_12011 = "96423986-0e1c-4ced-9f39-6d50ba9df1e5"; // LEASIDE AVE S & 2 AVE S
	private static final String STOP_12034 = "78232a7d-cc1d-4e3c-846d-cd3c79503fb8"; // 6 AVE S & 15 ST S
	private static final String STOP_12035 = "12b92405-7295-4879-9f4f-d1e32389d736"; // 6 Ave & 13 St S
	private static final String STOP_12038 = "831608ea-40ae-47e2-89bd-3dd62c26f431"; // 4 AVE S & 10 ST S
	private static final String STOP_12330 = "e2845963-1af1-49c7-8a7d-17fd2169c92a"; // 1 AVE S & 32 ST S
	private static final String STOP_13001 = "a4f2726f-4d71-46bb-8293-19b68f0346e5"; // University Terminal
	private static final String STOP_13002 = "b829138b-5aad-4776-8166-4f6b687630ee"; // University Terminal
	private static final String STOP_13004 = "50fbc070-7c47-4a50-bd0c-100e35e8d2ec"; // University Terminal
	private static final String STOP_13005 = "8e117892-4bf8-4803-b69a-510ba526019e"; // University Terminal
	private static final String STOP_13006 = "0e22d042-61fd-4dd3-9a75-ffe179bbac9c"; // Aperture Drive W
	private static final String STOP_13010 = "803bc9f6-ddfc-4dbd-b570-648283e4da23"; // University Dr W & Edgewood Blvd W
	private static final String STOP_13013 = "cea11d78-297d-4323-8088-50a4cf8e2d29"; // HERITAGE BLVD W & HERITAGE CL W
	private static final String STOP_13020 = "44951d0d-f5d2-4070-9454-83112a34e65c"; // HERITAGE BLVD & HERITAGE HEIGHTS PLAZA W
	private static final String STOP_13023 = "9cc047b8-e1c0-401b-887b-e500bd0cd2cc"; // HIGHLANDS BLVD W & ANGUS RD W
	private static final String STOP_13029 = "8e7cb21d-69ab-464e-b4b7-88aabde02355"; // RED CROW BLVD W & IROQUOIS CRES W
	private static final String STOP_13034 = "8b8a925a-9c60-4604-9ca0-a6238f403217"; // RED CROW BLVD W & JERRY POTTS BLVD W
	private static final String STOP_13039 = "ff88632a-218e-41ff-83cd-dc853a1d9f5c"; // RED CROW BLVD W & JERRY POTTS BLVD W
	private static final String STOP_13043 = "7c272bbc-0c70-4811-9bf8-16a7d81dc51e"; // COLUMBIA BLVD W & MODESTO RD W
	private static final String STOP_13061 = "b85adc85-ab66-417e-b009-a3ee9db10a90"; // UNIVERSITY DR W & COLUMBIA BLVD W
	private static final String STOP_13068 = "4ad38f45-79c3-48d3-b530-1bde2af9df59"; // COLUMBIA BLVD W & SIMON FRASER BLVD W
	private static final String STOP_13303 = "e80a149d-c250-4d0b-ad11-472d680c21ab"; // SILKSTONE RD W & SILKSTONE CRES W
	private static final String STOP_13311 = "341b4af2-1fe2-493e-83c3-7d8d5170c262"; // BRITANNIA BLVD W & AQUITANIA BLVD W
	private static final String STOP_13503 = "c6d3b3b3-4a23-4a2a-9289-a5f45cb62a2e"; // UNIVERSITY DR W & ROCKY MOUNTAIN BLVD WC
	private static final String STOP_13507 = "9ebfdcc6-650d-4d26-8240-63b36842c938"; // MT SUNDANCE RD W & MT SUNDIAL CRT W
	private static final String STOP_13517 = "c9a39dac-e10f-41cd-80d3-d98f3bf0200c"; // RIVERSTONE BLVD W
	private static final String STOP_13524 = "cbffbf8b-3b0d-4cb0-883b-4821ef2eabd9"; // Garry Station Prt W & Garry Dr W
	private static final String STOP_13450 = "45d0ae66-3b64-48ba-9b03-d282dfe1ecd9"; // GARRY DR W & SQUAMISH BLVD W
	private static final String STOP_14014 = "30461940-01b6-49e7-b087-0e9e980de6ca"; // City Center Terminal
	private static final String STOP_14016 = "fa70fca0-26eb-465c-8327-f09f30143245"; // City Center Terminal
	private static final String STOP_14034 = "10ec7bc6-1868-4b77-958e-090fda40c582"; // 4 Ave & 8 St S
	private static final String STOP_MESC2 = "507a74b8-e621-4953-ae84-6ea351fa25bf"; // 6 AVE & 13 ST S
	private static final String STOP_MESC3 = "fc844a65-fc34-493b-bcc5-298fb78c2eea"; // 6 AVE & 13 ST S

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
						STOP_14016, // == City Center Terminal
								STOP_14034, // !=
								STOP_12038, // ==
								STOP_12011, // !=
								STOP_12010, // == Transfer Point
								STOP_12330, // !=
								STOP_11267, // !=
								STOP_11271 // 14 AVE N & 39 ST N
						})) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						STOP_11271, // 14 AVE N & 39 ST N
								STOP_11268, // !=
								STOP_11054, // !=
								STOP_12010, // == Transfer Point
								STOP_12009, // !=
								STOP_12034, // ==
								STOP_MESC2, // !=
								STOP_MESC3, // !=
								STOP_12035, // ==
								STOP_14014 // City Center Terminal
						})) //
				.compileBothTripSort());
		map2.put(31l, new RouteTripSpec(31l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, NORTH_TERMINAL, //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, INDUSTRIAL) //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						STOP_11262, //
								STOP_11023, //
								STOP_11052 // North Terminal
						})) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						STOP_11052, // North Terminal
								STOP_11006, //
								STOP_11262 //
						})) //
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
						Arrays.asList(new String[] { STOP_13002, STOP_13006, STOP_13013, STOP_13020 })) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { STOP_13020, STOP_13023, STOP_13061, STOP_13002 })) //
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
						Arrays.asList(new String[] { //
						STOP_13507, //
								STOP_13517, //
								STOP_13004 // University Terminal
						})) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						STOP_13004, // University Terminal
								STOP_13503, //
								STOP_13507 //
						})) //
				.compileBothTripSort());
		map2.put(37l, new RouteTripSpec(37l, //
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Garry Sta") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { //
						STOP_13524, // Garry Station Prt W & Garry Dr W
								STOP_13450, // GARRY DR W & SQUAMISH BLVD W
								STOP_13004 // University Terminal
						})) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						STOP_13004, // University Terminal
								STOP_13010, // University Dr W & Edgewood Blvd W
								STOP_13524 // Garry Station Prt W & Garry Dr W
						})) //
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
