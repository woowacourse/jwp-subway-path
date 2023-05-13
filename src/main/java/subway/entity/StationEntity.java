package subway.entity;

import static java.util.stream.Collectors.toList;

import java.util.List;
import subway.domain.Line;

public class StationEntity {
    private Long id;
    private String name;
    private Long lineId;

    public StationEntity(String name, Long lineId) {
        this(null, name, lineId);
    }

    public StationEntity(final Long id, final String name, final Long lineId) {
        this.id = id;
        this.name = name;
        this.lineId = lineId;
    }

    public static List<StationEntity> of(final Line line, final Long lineId) {
        return line.findAllStation().stream()
                .map(station -> new StationEntity(station.getName(), lineId))
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getLineId() {
        return lineId;
    }
}
