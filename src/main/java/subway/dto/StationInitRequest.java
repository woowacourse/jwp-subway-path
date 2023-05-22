package subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class StationInitRequest {

    @NotBlank(message = "노선의 이름은 빈칸이 될 수 없습니다.")
    private String lineName;

    @NotBlank(message = "등록할 상행 종점 역의 이름이 빈칸이 될 수 없습니다.")
    private String upBoundStationName;

    @NotBlank(message = "등록할 하행 종점 역의 이름이 빈칸이 될 수 없습니다.")
    private String downBoundStationName;

    @Min(value = 0, message = "구간의 거리는 음수가 될 수 없습니다.")
    private int distance;

    public StationInitRequest() {
    }

    public StationInitRequest(final String lineName, final String upBoundStationName, final String downBoundStationName, final int distance) {
        this.lineName = lineName;
        this.upBoundStationName = upBoundStationName;
        this.downBoundStationName = downBoundStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
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
