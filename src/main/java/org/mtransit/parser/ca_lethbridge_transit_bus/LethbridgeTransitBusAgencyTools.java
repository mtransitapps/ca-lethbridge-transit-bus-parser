package org.mtransit.parser.ca_lethbridge_transit_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CharUtils;
import org.mtransit.commons.CleanUtils;
import org.mtransit.commons.StringUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.mt.data.MAgency;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mtransit.parser.StringUtils.EMPTY;

// http://opendata.lethbridge.ca/
// http://opendata.lethbridge.ca/datasets/e5ce3aa182114d66926d06ba732fb668
// https://www.lethbridge.ca/OpenDataSets/GTFS_Transit_Data.zip
public class LethbridgeTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@Nullable String[] args) {
		new LethbridgeTransitBusAgencyTools().start(args);
	}

	@Override
	public boolean defaultExcludeEnabled() {
		return true;
	}

	@NotNull
	@Override
	public String getAgencyName() {
		return "Lethbridge Transit";
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
		if (CharUtils.isDigitsOnly(gRoute.getRouteShortName())) {
			return Long.parseLong(gRoute.getRouteShortName()); // using route short name as route ID
		}
		final Matcher matcher = DIGITS.matcher(gRoute.getRouteShortName());
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
			if (CharUtils.isDigitsOnly(gRoute.getRouteShortName())) {
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

	@Override
	public boolean directionSplitterEnabled() {
		return true;
	}

	@Override
	public boolean directionSplitterEnabled(long routeId) {
		//noinspection RedundantIfStatement
		if (routeId == 20L + RID_STARTS_WITH_N // 20N
				|| routeId == 22L + RID_STARTS_WITH_N // 22N
				|| routeId == 22L + RID_STARTS_WITH_S // 22S
		) {
			return true; // OVERRIDE provided direction_id INVALID (used for "via " / trip variation)
		}
		return false;
	}

	@Override
	public boolean directionFinderEnabled() {
		return true;
	}

	private static final Pattern ENDS_WITH_LOOP = Pattern.compile("([\\s]*loop$)", Pattern.CASE_INSENSITIVE);
	private static final Pattern ENDS_WITH_ROUTE = Pattern.compile("([\\s]*route$)", Pattern.CASE_INSENSITIVE);

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = ENDS_WITH_LOOP.matcher(tripHeadsign).replaceAll(EMPTY);
		tripHeadsign = ENDS_WITH_ROUTE.matcher(tripHeadsign).replaceAll(EMPTY);
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
		if (CharUtils.isDigitsOnly(gStop.getStopCode())) {
			return Integer.parseInt(gStop.getStopCode()); // use stop code as stop ID
		}
		final Matcher matcher = DIGITS.matcher(gStop.getStopCode());
		if (matcher.find()) {
			int digits = Integer.parseInt(matcher.group());
			if (gStop.getStopCode().startsWith("MESC")) {
				return 13_050_000 + digits;
			}
		}
		throw new MTLog.Fatal("Unexpected stop ID for %s!", gStop);
	}
}
