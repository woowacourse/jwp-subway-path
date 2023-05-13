package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineRequest {

    @NotBlank(message = "노선 이름은 필수로 입력해야 합니다.")
    private String name;
    @NotBlank(message = "노선 색은 필수로 입력해야 합니다.")
    private String color;
    @NotNull(message = "노선 상행역은 필수로 입력해야 합니다.")
    private Long upStationId;
    @NotNull(message = "노선 하행역은 필수로 입력해야 합니다.")
    private Long downStationId;
    @Positive(message = "거리는 양수 값을 입력해야 합니다.")
    private Integer distance;

    private LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
