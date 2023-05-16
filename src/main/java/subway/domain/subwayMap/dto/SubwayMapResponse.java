package subway.domain.subwayMap.dto;

import java.util.List;

public class SubwayMapResponse {

    private List<SubwayMapForLineResponse> subwayMapForLineResponse;

    public SubwayMapResponse() {
    }

    public SubwayMapResponse(final List<SubwayMapForLineResponse> subwayMapForLineResponse) {
        this.subwayMapForLineResponse = subwayMapForLineResponse;
    }

    public List<SubwayMapForLineResponse> getSubwayMapResponse() {
        return subwayMapForLineResponse;
    }
}
