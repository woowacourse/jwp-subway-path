package subway.dto.section;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SectionCreateRequest {

    @NotNull(message = "노선 번호를 입력해주세요.")
    private Long lineNumber;

    @NotBlank(message = "상행 역을 입력해주세요.")
    private String upStation;

    @NotBlank(message = "하행 역을 입력해주세요.")
    private String downStation;

    @Min(value = 1, message = "역 간의 최소 거리는 1입니다.")
    @NotNull(message = "거리를 입력해주세요.")
    private Long distance;

    private SectionCreateRequest() {
    }

    public SectionCreateRequest(final Long lineNumber, final String upStation, final String downStation, final Long distance) {
        this.lineNumber = lineNumber;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
