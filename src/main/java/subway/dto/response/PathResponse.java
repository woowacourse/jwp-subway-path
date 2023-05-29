package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.fare.Fare;
import subway.domain.graph.Path;
import subway.domain.station.Station;
import subway.dto.StationDto;

public class PathResponse {
    private final List<StationDto> path;
    private final int distance;
    private final int fare;

    public PathResponse(final List<StationDto> path, final int distance, final int fare) {
        this.path = path;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(final List<Station> path, final int distance, final Fare fare) {
        List<StationDto> stationDtos = path.stream()
                .map(StationDto::from)
                .collect(Collectors.toList());

        return new PathResponse(stationDtos, distance, fare.getValue());
    }

    public static PathResponse from(final Path path) {
        List<StationDto> stationDtos = path.getStations().stream()
                .map(StationDto::from)
                .collect(Collectors.toList());

        return new PathResponse(stationDtos, path.getDistance().getValue(), path.getFare().getValue());
    }


    public List<StationDto> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
