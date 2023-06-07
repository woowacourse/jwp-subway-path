package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionDeleteRequest {
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String name;
    @NotNull(message = "노선 아이디는 필수 입력값입니다.")
    @Positive(message = "노선 아이디는 양수여야 합니다.")
    private Long lineId;

    public SectionDeleteRequest() {
    }

    public SectionDeleteRequest(String name, Long lineId) {
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
