package subway.service.domain;

public class StationRouteInfo {

    private Station previousStation;
    private LineProperty usedLine;
    private final Integer distance;

    public StationRouteInfo(Integer distance) {
        this.distance = distance;
    }

    public StationRouteInfo(Station previousStation, LineProperty usedLine, Integer distance) {
        this.previousStation = previousStation;
        this.usedLine = usedLine;
        this.distance = distance;
    }

    public Station getPreviousStation() {
        return previousStation;
    }

    public LineProperty getUsedLine() {
        return usedLine;
    }

    public Integer getDistance() {
        return distance;
    }

}

