package subway.ui.line.dto.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineCreateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @Positive
    @NotNull
    private Long upStationId;
    @Positive
    @NotNull
    private Long downStationId;
    @Positive
    private Long distance;

    private LineCreateRequest() {
    }

    public LineCreateRequest(final String name, final String color, final Long upStationId, final Long downStationId,
            final Long distance) {
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

    public Long getDistance() {
        return distance;
    }
}