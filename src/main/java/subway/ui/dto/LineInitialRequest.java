package subway.ui.dto;

public class LineInitialRequest {

    private final String name;
    private final String color;
    private final Integer distance;
    private final String firstStationName;
    private final String secondStationName;

    public LineInitialRequest(final String name, final String color, final Integer distance,
                              final String firstStationName, final String secondStationName) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.firstStationName = firstStationName;
        this.secondStationName = secondStationName;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getFirstStationName() {
        return firstStationName;
    }

    public String getSecondStationName() {
        return secondStationName;
    }

}
