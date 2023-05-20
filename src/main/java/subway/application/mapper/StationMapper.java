package subway.application.mapper;

import static subway.exception.ErrorCode.STATION_NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;
import subway.application.dto.StationResponse;
import subway.domain.line.LineWithSectionRes;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.domain.station.dto.StationRes;
import subway.exception.NotFoundException;

public class StationMapper {

    public static Station createStation(final Long stationId, final List<LineWithSectionRes> lineWithSections) {
        return lineWithSections.stream()
            .filter(res -> res.isSourceOrTargetStation(stationId))
            .findFirst()
            .map(res -> Station.create(res.getStationNameByStationId(stationId)))
            .orElseThrow(() -> new NotFoundException(
                STATION_NOT_FOUND,
                STATION_NOT_FOUND.getMessage() + " id = " + stationId));
    }

    public static List<StationResponse> createStationResponses(final List<LineWithSectionRes> possibleSections,
                                                               final List<Station> stations) {
        return stations.stream()
            .map(station -> new StationResponse(
                getStationIdByName(station.name(), possibleSections), station.name().name()))
            .collect(Collectors.toUnmodifiableList());
    }

    public static Long getStationIdByName(final StationName stationName,
                                          final List<LineWithSectionRes> lineWithSections) {
        return lineWithSections.stream()
            .filter(res -> res.isSourceOrTargetStation(stationName))
            .findFirst()
            .map(res -> res.getStationIdByStationName(stationName))
            .orElseThrow(() -> new NotFoundException(
                STATION_NOT_FOUND,
                STATION_NOT_FOUND.getMessage() + " name = " + stationName));
    }

    public static List<StationResponse> createStationResponses(final List<StationRes> findStations) {
        return findStations.stream()
            .map(res -> new StationResponse(res.getId(), res.getName()))
            .collect(Collectors.toUnmodifiableList());
    }
}
