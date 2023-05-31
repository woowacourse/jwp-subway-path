package subway.line.ui.dto;

import javax.validation.constraints.NotNull;

public class StationAdditionRequest {

    @NotNull(message = "추가하려는 역의 이름을 입력해주세요")
    private String stationName;
    @NotNull(message = "추가하려는 역의 상행역 ID를 입력해주세요")
    private Long upstreamId;
    @NotNull(message = "추가하려는 역의 하행역 ID를 입력해주세요")
    private Long downstreamId;
    @NotNull(message = "추가하려는 역과 상행역의 거리를 입력해주세요")
    private int distanceToUpstream;

    public StationAdditionRequest(String stationName, Long upstreamId, Long downstreamId, Integer distanceToUpstream) {
        this.stationName = stationName;
        this.upstreamId = upstreamId;
        this.downstreamId = downstreamId;
        this.distanceToUpstream = distanceToUpstream;
    }

    public String getStationName() {
        return stationName;
    }

    public Long getUpstreamId() {
        return upstreamId;
    }

    public Long getDownstreamId() {
        return downstreamId;
    }

    public int getDistanceToUpstream() {
        return distanceToUpstream;
    }
}
