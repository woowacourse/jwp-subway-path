package subway.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionCreateRequest {

    @NotBlank(message = "역 이름은 빈 값이 될 수 없습니다.")
    private String leftStationName;

    @NotBlank(message = "역 이름은 빈 값이 될 수 없습니다.")
    private String rightStationName;

    @NotNull(message = "거리 값은 빈 값이 될 수 없습니다.")
    @Positive
    private Integer distance;

    private SectionCreateRequest() {
    }

    public SectionCreateRequest(String leftStationName, String rightStationName, Integer distance) {
        this.leftStationName = leftStationName;
        this.rightStationName = rightStationName;
        this.distance = distance;
    }

    public String getLeftStationName() {
        return leftStationName;
    }

    public String getRightStationName() {
        return rightStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
