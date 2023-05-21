package subway.dto.line;

import java.util.List;
import javax.validation.constraints.NotNull;
import subway.domain.line.Line;
import subway.domain.line.Section;

public class LineCreateRequest {

    @NotNull
    private final String lineName;
    @NotNull
    private final String sourceStation;
    @NotNull
    private final String targetStation;
    @NotNull
    private final Integer distance;

    public LineCreateRequest(String lineName, String sourceStation, String targetStation, Integer distance) {
        this.lineName = lineName;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
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
