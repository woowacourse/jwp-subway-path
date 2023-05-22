package subway.dto;

import javax.validation.constraints.NotBlank;

public class PathRequest {
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String sourceStation;
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String targetStation;

    public PathRequest() {
    }

    public PathRequest(String sourceStation, String targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getTargetStation() {
        return targetStation;
    }
}
