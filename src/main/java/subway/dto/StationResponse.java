package subway.dto;

import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private final Long id;
    private final String name;

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static List<StationResponse> listOf(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
