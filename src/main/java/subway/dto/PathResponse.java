package subway.dto;

import java.util.List;

public class PathResponse {
    private final Integer distance;
    private final List<FareResponse> fareResponses;
    private final List<SectionResponse> path;

    public PathResponse(Integer distance, List<FareResponse> fareResponses, List<SectionResponse> path) {
        this.distance = distance;
        this.fareResponses = fareResponses;
        this.path = path;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<FareResponse> getFareResponses() {
        return fareResponses;
    }

    public List<SectionResponse> getPath() {
        return path;
    }
}
