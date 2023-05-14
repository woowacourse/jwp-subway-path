package subway.subwayMap.dto;

import java.util.List;

public class SubwayMapResponse {

    private List<SubwayMapForLineResponse> subwayMapForLineRespons;

    public SubwayMapResponse() {
    }

    public SubwayMapResponse(final List<SubwayMapForLineResponse> subwayMapForLineRespons) {
        this.subwayMapForLineRespons = subwayMapForLineRespons;
    }

    public List<SubwayMapForLineResponse> getSubwayMapResponses() {
        return subwayMapForLineRespons;
    }
}
