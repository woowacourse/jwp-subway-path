package subway.domain;

import subway.entity.LineEntity;

import java.util.Objects;

public class Line {
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MAX_NAME_LENGTH = 10;

    private final Long id;
    private final String name;

    public Line(final Long id, final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("노선 이름은 1자 이상 10자 이하입니다.");
        }
        this.id = id;
        this.name = name;
    }

    public Line(final String name) {
        this(null, name);
    }

    public LineEntity toEntity() {
        return new LineEntity(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                '}';
    }
}
