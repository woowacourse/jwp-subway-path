package subway.dto.response;

import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private long id;
    private String name;

    private StationResponse() {
    }

    private StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static StationResponse of(final Long id, final String name) {
        return new StationResponse(id, name);
    }

    public static List<StationResponse> from(final List<Station> orderedStations) {
        return orderedStations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
