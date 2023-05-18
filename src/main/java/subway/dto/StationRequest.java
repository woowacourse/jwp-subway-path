package subway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "등록할 역의 이름은 빈 칸이면 안됩니다.")
    private String registerStationName;

    @NotBlank(message = "노선의 이름은 빈 칸이면 안됩니다.")
    private String lineName;

    @NotBlank(message = "기준 역이 이름은 빈 칸이면 안됩니다.")
    private String baseStation;

    @NotBlank(message = "기준 역에 대한 방향은 빈 칸이면 안됩니다.")
    private String direction;

    @Min(value = 0, message = "구간의 거리는 음수가 될 수 없습니다.")
    private int distance;

    public StationRequest() {
    }

    public StationRequest(
            final String registerStationName,
            final String lineName,
            final String baseStation,
            final String direction,
            final int distance
    ) {
        this.registerStationName = registerStationName;
        this.lineName = lineName;
        this.baseStation = baseStation;
        this.direction = direction;
        this.distance = distance;
    }

    public String getRegisterStationName() {
        return registerStationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public String getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }
}
