package subway.subwayMap.dto;

import java.util.List;

public class SubwayMapResponses {

    private List<SubwayMapResponse> subwayMapResponses;

    public SubwayMapResponses() {
    }

    public SubwayMapResponses(final List<SubwayMapResponse> subwayMapResponses) {
        this.subwayMapResponses = subwayMapResponses;
    }

    public List<SubwayMapResponse> getSubwayMapResponses() {
        return subwayMapResponses;
    }
}
