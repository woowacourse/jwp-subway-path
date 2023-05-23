package subway.ui.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class LineRequest {

    @NotBlank
    private final String name;
    @NotBlank
    private final String color;
    @Positive
    private final Long upStationId;
    @Positive
    private final Long downStationId;
    @Positive
    private final Integer distance;
    @Min(0)
    private final Integer additionalCharge;

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance,
        Integer additionalCharge) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.additionalCharge = additionalCharge;
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

    public Integer getAdditionalCharge() {
        return additionalCharge;
    }
}
