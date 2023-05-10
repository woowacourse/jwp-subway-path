package subway.domain;

import java.util.Objects;

public class Line {
    private Long id;
    private String name;
    private String color;
    private Long upEndpointId;
    private Long downEndpointId;

    public Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line(final Long id, final String name, final String color, final Long upEndpointId, final Long downEndpointId) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upEndpointId = upEndpointId;
        this.downEndpointId = downEndpointId;
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

    public Long getDownEndpointId() {
        return downEndpointId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }
}
