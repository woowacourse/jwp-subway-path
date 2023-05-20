package subway.dto;

import java.util.List;

public final class PathResponse {

    private List<SectionResponse> sectionResponses;
    private int distance;
    private int cost;

    public PathResponse(List<SectionResponse> sectionResponses, int distance, int cost) {
        this.sectionResponses = sectionResponses;
        this.distance = distance;
        this.cost = cost;
    }

    public List<SectionResponse> getSectionResponses() {
        return sectionResponses;
    }

    public int getDistance() {
        return distance;
    }

    public int getCost() {
        return cost;
    }
}
