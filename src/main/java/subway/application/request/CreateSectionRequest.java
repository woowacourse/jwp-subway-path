package subway.application.request;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateSectionRequest {

    @NotNull(message = "역명은 null일 수 없습니다.")
    @Length(min = 1, max = 10, message = "역명의 길이는 1 ~ 10이하이어야 합니다.")
    private String upStationName;

    @NotNull(message = "역명은 null일 수 없습니다.")
    @Length(min = 1, max = 10, message = "역명의 길이는 1 ~ 10이하이어야 합니다.")
    private String downStationName;

    @NotNull(message = "노선 식별자값은 null일 수 없습니다.")
    @Positive(message = "노선 식별자값은 양수이어야 합니다.")
    private Long lineId;

    @NotNull(message = "노선 식별자값은 null일 수 없습니다.")
    @Positive(message = "노선 식별자값은 양수이어야 합니다.")
    private Integer distance;

    public CreateSectionRequest() {
    }

    public CreateSectionRequest(final String upStationName, final String downStationName, final Long lineId, final Integer distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.lineId = lineId;
        this.distance = distance;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }
}
