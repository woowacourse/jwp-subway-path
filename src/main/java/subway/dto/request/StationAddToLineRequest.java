package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class StationAddToLineRequest {

    @NotBlank(message = "노선을 입력해주세요.")
    private String lineName;

    @NotBlank(message = "상행역 입력해주세요.")
    private String upStationName;

    @NotBlank(message = "하행역 입력해주세요.")
    private String downStationName;

    @NotNull(message = "역간의 거리를 입력해주세요.")
    private Integer distance;

    private StationAddToLineRequest() {
    }

    public StationAddToLineRequest(final String lineName, final String upStationName,
                                   final String downStationName, final Integer distance) {
        this.lineName = lineName;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }


    public String getLineName() {
        return lineName;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
