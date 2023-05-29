package subway.dto;

import java.util.List;

public final class PathResponse {

    private List<String> path;
    private Long distance;
    private Long charge;

    public PathResponse(final List<String> path, final Long length, final Long charge) {
        this.path = path;
        this.distance = length;
        this.charge = charge;
    }

    public List<String> getPath() {
        return path;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getCharge() {
        return charge;
    }
}
