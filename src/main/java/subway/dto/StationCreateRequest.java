package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class StationCreateRequest {
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String name;

    @NotNull(message = "라인 ID는 필수 입력 값입니다.")
    @Positive
    private Long lineId;

    private StationCreateRequest() {
    }

    public StationCreateRequest(final String name, final Long lineId) {
        this.name = name;
        this.lineId = lineId;
    }

    public String getName() {
        return name;
    }

    public Long getLineId() {
        return lineId;
    }
}
