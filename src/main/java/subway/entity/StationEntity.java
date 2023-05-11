package subway.entity;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(String name) {
        this(null, name);
    }

    public StationEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<StationEntity> of(Line line) {
        return line.getStations().stream()
                .map(station -> new StationEntity(station.getName()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
