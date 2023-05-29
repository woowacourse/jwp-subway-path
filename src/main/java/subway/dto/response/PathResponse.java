package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.station.Station;
import subway.domain.fare.Fare;
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
