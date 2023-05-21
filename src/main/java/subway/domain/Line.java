package subway.domain;

import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Integer extraFee;

    public Line(final String name, final String color, final Integer extraFee) {
        this(null, name, color, extraFee);
    }

    public Line(final Long id, final String name, final String color, final Integer extraFee) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFee = extraFee;
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

    public Integer getExtraFee() {
        return extraFee;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color) && Objects.equals(extraFee, line.extraFee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, extraFee);
    }
}
