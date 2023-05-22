package subway.application.core.service.dto.out;

import subway.application.core.domain.Station;

public class StationResult {

    private final Long id;
    private final String name;

    public StationResult(Station station) {
        this(station.getId(), station.getName());
    }

    public StationResult(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
