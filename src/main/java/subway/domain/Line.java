package subway.domain;

public class Line {

    private final String name;
    private final String color;
    private final Stations stations;

    public Line(String name, String color, Stations stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Stations getStations() {
        return stations;
    }
}
