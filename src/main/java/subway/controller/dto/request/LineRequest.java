package subway.controller.dto.request;

public class LineRequest {

    private final String name;
    private final String color;
    private final Integer distance;
    private final String firstStation;
    private final String secondStation;

    public LineRequest(final String name, final String color, final Integer distance,
                       final String firstStation, final String secondStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.firstStation = firstStation;
        this.secondStation = secondStation;
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

    public String getFirstStation() {
        return firstStation;
    }

    public String getSecondStation() {
        return secondStation;
    }

}
