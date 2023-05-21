package subway.dto;

import subway.domain.Path.Path;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.dto.response.LineResponse;
import subway.dto.response.LineStationsResponse;
import subway.dto.response.PathResponse;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static StationResponse convertToStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static LineResponse convertToLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    public static List<StationResponse> convertToStationResponses(List<Station> stations) {
        return stations.stream()
                .map(DtoMapper::convertToStationResponse)
                .collect(Collectors.toList());
    }

    public static LineStationsResponse convertToLineStationsResponse(Line line, List<Station> inOrderLineStations) {
        return new LineStationsResponse(
                convertToLineResponse(line),
                convertToStationResponses(inOrderLineStations)
        );
    }

    public static PathResponse convertToPathResponse(Path path) {
        List<StationResponse> stationResponses = convertToStationResponses(path.getOrderedStations());
        return new PathResponse(stationResponses, path.getDistance(), path.getFare());
    }
}
