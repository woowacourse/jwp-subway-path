package subway.dto;

import java.util.List;
import subway.domain.Charge;
import subway.domain.Distance;

public class PathResponse {

    private List<String> path;
    private int distance;
    private int charge;
    private int teenagerCharge;
    private int childCharge;

    public PathResponse() {
    }

    public static PathResponse of(List<String> path, Distance distance, Charge charge,
        Charge teenagerCharge, Charge childCharge) {
        return new PathResponse(path, distance.getValue(), charge.getValue(),
            teenagerCharge.getValue(), childCharge.getValue());
    }

    public PathResponse(List<String> path, int distance, int charge, int teenagerCharge,
        int childCharge) {
        this.path = path;
        this.distance = distance;
        this.charge = charge;
        this.teenagerCharge = teenagerCharge;
        this.childCharge = childCharge;
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

    public int getTeenagerCharge() {
        return teenagerCharge;
    }

    public int getChildCharge() {
        return childCharge;
    }
}
