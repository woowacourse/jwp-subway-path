package subway.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(
        description = "지하철 승객 정보",
        example = "{\"age\": 15, \"startStationId\": 1, \"endStationId\": 2}"
)
public class PassengerRequest {

    @Schema(description = "탑승자 나이")
    @NotNull(message = "탑승자 나이는 입력해야 합니다.")
    @Min(value = 1, message = "탑승자 나이는 0보다 커야합니다.")
    private Integer age;

    @Schema(description = "출발역 ID")
    @NotNull(message = "출발역 ID는 존재해야 합니다.")
    private Long startStationId;

    @Schema(description = "도착역 ID")
    @NotNull(message = "도착역 ID는 존재해야 합니다.")
    private Long endStationId;

    private PassengerRequest() {
    }

    public PassengerRequest(final Integer age, final Long startStationId, final Long endStationId) {
        this.age = age;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Integer getAge() {
        return age;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
