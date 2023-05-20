package subway.domain;

public class Line {
    //TODO: stations 제거?? 동시성 문제. id도 마찬가지임.
    private static Long sequence = 1L;
    private final Long id;
    private final Stations stations;
    private String name;
    private String color;

    public Line(String name, String color, Stations stations) {
        validateStations(stations);
        this.id = sequence++;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }


    private void validateStations(Stations stations) {
        if (stations.getStationsSize() < 2) {
            throw new IllegalArgumentException("노선 생성시 역을 2개 입력해야합니다");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Stations getStations() {
        return stations;
    }
}
