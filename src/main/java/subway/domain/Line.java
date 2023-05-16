package subway.domain;

public class Line {

    private final String name;
    private final String color;
    private final Stations stations;

    public Line(String name, String color, Stations stations) {
        validateStations(stations);
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    private void validateStations(Stations stations) {
        if (stations.getStationsSize() < 2) {
            throw new IllegalArgumentException("노선 생성시 역을 2개 입력해야합니다");
        }
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
