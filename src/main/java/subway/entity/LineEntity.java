package subway.entity;

import java.util.Objects;
import subway.domain.line.Line;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final int fare;

    public LineEntity(final String name, final String color, final int fare) {
        this(null, name, color, fare);
    }

    public LineEntity(final Long id, final String name, final String color, final int fare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.fare = fare;
    }

    public static LineEntity from(final Line line) {
        return new LineEntity(line.getId(), line.getName(), line.getColor(), line.getFare());
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

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LineEntity that = (LineEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
