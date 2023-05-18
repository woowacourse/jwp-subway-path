package subway.dto;

import java.util.List;
import subway.domain.Charge;
import subway.domain.Distance;

public class PathResponse {

    private List<String> path;
    private int distance;
    private int charge;

    public PathResponse() {
    }
    public static PathResponse of(List<String> path, Distance distance, Charge charge) {
        return new PathResponse(path, distance.getValue(), charge.getValue());
    }
    public PathResponse(List<String> path, int distance, int charge) {
        this.path = path;
        this.distance = distance;
        this.charge = charge;
    }

    public List<String> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public int getCharge() {
        return charge;
    }
}
