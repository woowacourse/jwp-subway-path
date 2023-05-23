package subway.service.domain;

public class PathElement {

    private final Station station;
    private final Integer distance;

    public PathElement(Station station, Integer distance) {
        this.station = station;
        this.distance = distance;
    }

    public Station getStation() {
        return station;
    }

    public Integer getDistance() {
        return distance;
    }

}
