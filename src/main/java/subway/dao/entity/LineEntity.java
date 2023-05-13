package subway.dao.entity;

import subway.domain.Line;
import subway.domain.Station;

import java.util.Optional;

public class LineEntity {
    private final Long id;
    private final String name;
    private final String color;
    private final Long upEndpointId;

    public LineEntity(final String name, final String color) {
        this(null, name, color, null);
    }

    public LineEntity(final Long id, final String name, final String color, final Long upEndpointId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upEndpointId = upEndpointId;
    }

    public LineEntity(Line line, Long upEndpointId) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.upEndpointId = upEndpointId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpEndpointId() {
        return upEndpointId;
    }

    public Line toLine() {
        return new Line(id, name, color);
    }
}
