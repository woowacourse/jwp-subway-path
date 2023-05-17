package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class StationResponse {

    private final String lineName;
    private final List<String> stationNames;

    public StationResponse(final String lineName, final List<String> stationNames) {
        this.lineName = lineName;
        this.stationNames = stationNames;
    }

    public static StationResponse of(final Line line, final List<Station> stations) {
        final List<String> stationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
        return new StationResponse(line.getName(), stationNames);
    }
}
