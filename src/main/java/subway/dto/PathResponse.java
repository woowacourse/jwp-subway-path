package subway.dto;

import java.util.List;

public class PathResponse {

    private final Integer distance;
    private final Integer charge;
    private final List<SectionResponse> paths;

    public PathResponse(final Integer distance, final Integer charge, final List<SectionResponse> paths) {
        this.distance = distance;
        this.charge = charge;
        this.paths = paths;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getCharge() {
        return charge;
    }

    public List<SectionResponse> getPaths() {
        return paths;
    }
}
