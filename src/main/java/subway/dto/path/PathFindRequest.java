package subway.dto.path;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotBlank;
import subway.application.dto.PathFindDto;

public class PathFindRequest {
    @NotBlank(message = "출발역은 비어있을 수 없습니다.")
    private String sourceStationName;
    @NotBlank(message = "도착역은 비어있을 수 없습니다.")
    private String destStationName;

    public PathFindRequest() {
    }

    @JsonCreator
    public PathFindRequest(String sourceStationName, String destStationName) {
        this.sourceStationName = sourceStationName;
        this.destStationName = destStationName;
    }

    public PathFindDto toPathFindDto() {
        return new PathFindDto(sourceStationName, destStationName);
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getDestStationName() {
        return destStationName;
    }
}
