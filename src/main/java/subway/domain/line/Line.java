package subway.domain.line;

import subway.domain.Path.Fare;

import java.util.Objects;

public class Line {

    private final LineName name;
    private final LineColor color;
    private final Fare extraFare;

    private Line(LineName name, LineColor color, Fare extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public static Line of(String name, String color, int extraFare) {
        return new Line(LineName.from(name), LineColor.from(color), Fare.from(extraFare));
    }

    public boolean isSameName(Line line) {
        return this.name.equals(line.name);
    }

    public boolean isSameColor(Line line) {
        return this.color.equals(line.color);
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
        Line other = (Line) o;
        return Objects.equals(name, other.name)
                && Objects.equals(color, other.color)
                && Objects.equals(extraFare, other.extraFare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, extraFare);
    }
}
