package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class StationInitialSaveRequest {

    @NotBlank(message = "노선명을 입력해주세요.")
    private String lineName;

    @NotBlank(message = "시작역을 입력해주세요.")
    private String leftStationName;

    @NotBlank(message = "도착역을 입력해주세요.")
    private String rightStationName;

    @Positive(message = "등록할 거리는 양수여야 합니다.")
    private Integer distance;

    private StationInitialSaveRequest() {
    }

    public StationInitialSaveRequest(
            final String lineName,
            final String leftStationName,
            final String rightStationName,
            final Integer distance
    ) {
        this.lineName = lineName;
        this.leftStationName = leftStationName;
        this.rightStationName = rightStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
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
