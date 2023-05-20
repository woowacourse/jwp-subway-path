package subway.dto.request;

import javax.validation.constraints.NotEmpty;
import subway.dto.PathDto;

public class ReadPathRequest {

    @NotEmpty(message = "출발역 이름이 입력되지 않았습니다.")
    private final String sourceStation;

    @NotEmpty(message = "도착역 이름이 입력되지 않았습니다.")
    private final String targetStation;

    public ReadPathRequest(final String sourceStation, final String targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public ReadPathRequest() {
        this(null, null);
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getTargetStation() {
        return targetStation;
    }

    public PathDto toDto() {
        return new PathDto(sourceStation, targetStation);
    }
}
