package subway.dto;

import java.util.List;
import javax.validation.constraints.NotNull;
import subway.domain.Line;
import subway.domain.Section;

public class LineRequest {

    @NotNull(message = "노선 이름을 입력해주세요")
    private String lineName;
    @NotNull(message = "출발역을 입력해주세요")
    private String sourceStation;
    @NotNull(message = "도착역을 입력해주세요")
    private String targetStation;
    @NotNull(message = "거리를 입력해주세요")
    private Integer distance;

    public LineRequest(String lineName, String sourceStation, String targetStation, Integer distance) {
        this.lineName = lineName;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public LineRequest() {
    }

    public String getLineName() {
        return lineName;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getTargetStation() {
        return targetStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public Line toDomain() {
        return new Line(lineName, List.of(new Section(sourceStation, targetStation, distance)));
    }
}
