package subway.ui.dto;

import javax.validation.constraints.NotNull;

public class LineCreationRequest {

    @NotNull(message = "생성하려는 노선 이름을 입력해주세요")
    private String lineName;
    @NotNull(message = "노선의 상행 종점을 입력해주세요")
    private String upstreamName;
    @NotNull(message = "노선의 하행 종점을 입력해주세요")
    private String downstreamName;
    @NotNull(message = "노선의 상행 종점과 하행 종점의 거리를 입력해주세요")
    private int distance;

    public LineCreationRequest(String lineName, String upstreamName, String downstreamName, int distance) {
        this.lineName = lineName;
        this.upstreamName = upstreamName;
        this.downstreamName = downstreamName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpstreamName() {
        return upstreamName;
    }

    public String getDownstreamName() {
        return downstreamName;
    }

    public int getDistance() {
        return distance;
    }
}
