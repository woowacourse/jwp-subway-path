package subway.domain;

import java.util.Objects;

public final class Line {

    private Long id;
    private String name;
    private String color;
    private Integer additionalFee;

    Line() {
    }

    private Line(Long id, String name, String color, Integer additionalFee) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFee = additionalFee;
    }

    public static Line of(final Long id, final String name, final String color, final Integer additionalFee) {
        return new Line(id, name, color, additionalFee);
    }

    public static Line withNullId(final String name, final String color, final Integer additionalFee) {
        return new Line(null, name, color, additionalFee);
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

    public Integer getAdditionalFee() {
        return additionalFee;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        if (Objects.isNull(id) || Objects.isNull(line.id)) {
            return false;
        }
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
