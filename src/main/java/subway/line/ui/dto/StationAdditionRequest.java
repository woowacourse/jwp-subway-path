package subway.line.ui.dto;

import javax.validation.constraints.NotNull;

public class StationAdditionRequest {

    @NotNull(message = "추가하려는 역의 이름을 입력해주세요")
    private String stationName;
    @NotNull(message = "추가하려는 역의 상행역을 입력해주세요")
    private String upstreamName;
    @NotNull(message = "추가하려는 역의 하행역을 입력해주세요")
    private String downstreamName;
    @NotNull(message = "추가하려는 역과 상행역의 거리를 입력해주세요")
    private int distanceToUpstream;

    public StationAdditionRequest(String stationName, String upstreamName, String downstreamName, int distanceToUpstream) {
        this.stationName = stationName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distanceToUpstream = distanceToUpstream;
    }

    public String getStationName() {
        return stationName;
    }

    public String getUpstreamName() {
        return upstreamName;
    }

    public String getDownstreamName() {
        return downstreamName;
    }

    public int getDistanceToUpstream() {
        return distanceToUpstream;
    }
}
