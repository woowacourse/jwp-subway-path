package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class LineStationRequest {
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String upBoundStationName;
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String downBoundStationName;
    @Positive(message = "0이하일 수 없습니다.")
    private int distance;

    public LineStationRequest() {
    }

    public LineStationRequest(String upBoundStationName, String downBoundStationName, int distance) {
        this.upBoundStationName = upBoundStationName;
        this.downBoundStationName = downBoundStationName;
        this.distance = distance;
    }

    public String getUpBoundStationName() {
        return upBoundStationName;
    }

    public String getDownBoundStationName() {
        return downBoundStationName;
    }

    public int getDistance() {
        return distance;
    }
}
