package subway.ui.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineRequest {

    @NotBlank(message = "name 필드가 있어야 합니다.")
    private String name;

    @NotBlank(message = "color 필드가 있어야 합니다.")
    private String color;

    @NotNull(message = "상행 역을 등록해야 합니다.")
    private Long upStationId;

    @NotNull(message = "하행 역을 등록해야 합니다.")
    private Long downStationId;

    @Positive(message = "역간 거리는 양의 정수여야 합니다.")
    private int distance;

    public LineRequest() {
    }

    public LineRequest(
            final String name,
            final String color,
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
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

    public int getDistance() {
        return distance;
    }
}
