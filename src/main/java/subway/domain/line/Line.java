package subway.domain.line;

import subway.domain.Path.Fare;

import java.util.Objects;

public class Line {

    private final Long id;
    private final LineName name;
    private final LineColor color;
    private final Fare extraFare;

    private Line(Long id, LineName name, LineColor color, Fare extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public static Line of(Long id, String name, String color, int extraFare) {
        return new Line(id, LineName.from(name), LineColor.from(color), Fare.from(extraFare));
    }

    public static Line of(String name, String color, int extraFare) {
        return new Line(null, LineName.from(name), LineColor.from(color), Fare.from(extraFare));
    }

    public boolean isSameName(Line line) {
        return this.name.equals(line.name);
    }

    public boolean isSameColor(Line line) {
        return this.color.equals(line.color);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getLineName();
    }

    public String getColor() {
        return color.getLineColor();
    }

    public int getExtraFare() {
        return extraFare.getFare();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        if (this.id != null && line.id != null) {
            return this.id.equals(line.id);
        }
        return Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

}
