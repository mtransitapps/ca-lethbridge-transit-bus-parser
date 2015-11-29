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
// http://www.lethbridge.ca/OpenDataSets/GTFS_TransitData_30June_2015.zip
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
			if (gRoute.getRouteShortName().toLowerCase(Locale.ENGLISH).endsWith("n")) {
				return RID_STARTS_WITH_N + digits;
			} else if (gRoute.getRouteShortName().toLowerCase(Locale.ENGLISH).endsWith("s")) {
				return RID_STARTS_WITH_S + digits;
			}
		}
		System.out.printf("\nUnexpected route ID for %s!\n", gRoute);
		System.exit(-1);
		return -1l;
	}

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
				case 12: return "University / Downtown";
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
				return "Henderson Lk / Industrial";
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
			if ("80FF00".equalsIgnoreCase(gRoute.getRouteColor())) {
				return "81CC2B";
			}
		} else if ("36".equalsIgnoreCase(gRoute.getRouteShortName())) {
			if ("80FF80".equalsIgnoreCase(gRoute.getRouteColor())) {
				return "4DB8A4";
			}
		}
		return super.getRouteColor(gRoute);
	}

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;
	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<Long, RouteTripSpec>();
		map2.put(21l + RID_STARTS_WITH_N, new RouteTripSpec(21l + RID_STARTS_WITH_N, // 21N
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Nord-Bridge", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "City Ctr") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { //
						"ffcc6237-0c3a-4e0f-b6f0-111c1455c114", //
								"73f68ed8-7298-462a-9877-fd9ce77d3391", //
								"02e03542-8175-4da6-b0c7-66242bc2bd28" })) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						"02e03542-8175-4da6-b0c7-66242bc2bd28", //
								"aa5ad435-3df7-4e9c-86bd-0c44a67a1c0e", //
								"d80511d8-ffc8-419b-8784-d8a4df67b283" })) //
				.compileBothTripSort());
		map2.put(21l + RID_STARTS_WITH_S, new RouteTripSpec(21l + RID_STARTS_WITH_S, // 21S
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Henderson Lk / Industrial", //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "City Ctr") //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						"d80511d8-ffc8-419b-8784-d8a4df67b283", //
								"7ccfd0cb-5acd-4f85-9300-5012d7586dc5", //
								"c85ea559-f07a-4bef-943a-6dbdf3a6894c", //
								"a820f6d7-19c9-4564-bd84-ecc5a3d0c2ff", //
								"38e1e7bf-9c74-49f1-843e-c8e9765dc343", //
								"5dd42d30-bffb-496a-aee2-1720906bb6c1", //
								"ec942f99-3e6d-437c-b3fe-4d1ebf42e767" })) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						"ec942f99-3e6d-437c-b3fe-4d1ebf42e767", //
								"9af5b810-30cc-4f4b-a9f3-cf67205df281", //
								"38e1e7bf-9c74-49f1-843e-c8e9765dc343", //
								"f1f710cc-c3fa-4a6a-9123-3c9f6ba784f3", //
								"4ead3172-12e6-4cac-844d-38b53b3deb30", //
								"888d39d4-41ac-4c0a-b27e-c05d405e94ca", //
								"27b55fa2-78cc-470e-85c5-1514e6f8d2c2", //
								"d89718f4-d5c6-44e8-85c8-605e58577fda", //
								"ffcc6237-0c3a-4e0f-b6f0-111c1455c114" })) //
				.compileBothTripSort());
		map2.put(31l, new RouteTripSpec(31l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "North Terminal", //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Industrial") //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						"ca7744ba-6245-4f39-a33f-451c742bed77", //
								"fea10750-80b7-43b3-b242-77db78c34f32", //
								"10708ae5-f0c9-42f4-942b-47e2dff24aad" })) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						"10708ae5-f0c9-42f4-942b-47e2dff24aad", //
								"49a5bafa-574d-43c0-841f-fb3c57029cc6", //
								"ca7744ba-6245-4f39-a33f-451c742bed77" })) //
				.compileBothTripSort());
		map2.put(32l, new RouteTripSpec(32l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University", //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Indian Battle") //
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						"c9dfc721-5519-4e21-a74f-5e643b48450f", //
								"7480cc6d-1b13-4b62-8a8d-ae8900cd858f", //
								"da5a76e6-2743-43f1-ab7d-d4eb19013c73", //
								"25ec1c3d-e01f-4075-8c3a-84f697911b70" })) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						"469885f5-198d-4c32-ab9b-e2392c577e25", //
								"f687cda6-e8ea-401f-a2cc-b6d827a2d169", //
								"c9dfc721-5519-4e21-a74f-5e643b48450f" })) //
				.compileBothTripSort());
		map2.put(33l, new RouteTripSpec(33l, //
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Heritage", // "West Highlands"
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { //
						"0be2a39a-5c45-4d2e-878c-5b1908bf1fb6", //
								"13c81b83-d3e1-41c9-8681-3f0ed52728c7", //
								"01b4da4b-37b4-42ee-90fb-2106eae877e2" })) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						"01b4da4b-37b4-42ee-90fb-2106eae877e2", //
								"cab0cc4d-9b06-48e5-868d-57bbe998a0be", //
								"0be2a39a-5c45-4d2e-878c-5b1908bf1fb6" })) //
				.compileBothTripSort());
		map2.put(35l, new RouteTripSpec(35l, //
				MDirectionType.EAST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University", //
				MDirectionType.WEST.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Crossings") // "Copperwood"
				.addTripSort(MDirectionType.EAST.intValue(), //
						Arrays.asList(new String[] { //
						"aaf27166-8767-491b-94a6-3787d263d638", //
								"590a3092-4ebf-4024-a84d-7948cba269ac", //
								"94bc6566-8759-4c74-9783-c3198babc714" })) //
				.addTripSort(MDirectionType.WEST.intValue(), //
						Arrays.asList(new String[] { //
						"94bc6566-8759-4c74-9783-c3198babc714", //
								"ae7d4c36-64c2-402a-a6dd-d62f55752326", //
								"aaf27166-8767-491b-94a6-3787d263d638" })) //
				.compileBothTripSort());
		map2.put(36l, new RouteTripSpec(36l, //
				MDirectionType.NORTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "University", //
				MDirectionType.SOUTH.intValue(), MTrip.HEADSIGN_TYPE_STRING, "Sunridge") //
				.addTripSort(MDirectionType.NORTH.intValue(), //
						Arrays.asList(new String[] { //
						"cb28e42d-9385-4be6-8452-49f0e59be075", //
								"606c7d8d-0d0d-4b13-a15e-b76142c7ab73", //
								"94bc6566-8759-4c74-9783-c3198babc714" })) //
				.addTripSort(MDirectionType.SOUTH.intValue(), //
						Arrays.asList(new String[] { //
						"94bc6566-8759-4c74-9783-c3198babc714", //
								"25bfb3ad-c2dd-4928-a352-ffb64539dbe4", //
								"cb28e42d-9385-4be6-8452-49f0e59be075" })) //
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

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		String tripHeadsign = gTrip.getTripHeadsign();
		if (StringUtils.isEmpty(tripHeadsign)) {
			tripHeadsign = mRoute.getLongName();
		}
		mTrip.setHeadsignString(cleanTripHeadsign(tripHeadsign), gTrip.getDirectionId());
	}

	private static final Pattern UNIVERSITY_OF = Pattern.compile("((^|\\W){1}(university of)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String UNIVERSITY_OF_REPLACEMENT = "$2U of$4";

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
