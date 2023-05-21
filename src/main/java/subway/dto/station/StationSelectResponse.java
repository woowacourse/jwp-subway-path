package subway.dto.station;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Station;
import subway.entity.StationEntity;

public class StationSelectResponse {
    private final Long id;
    private final String name;

    public StationSelectResponse(final String name) {
        this(null, name);
    }

    public StationSelectResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationSelectResponse from(StationEntity station) {
        return new StationSelectResponse(station.getId(), station.getName());
    }

    public static List<StationSelectResponse> from(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationSelectResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
