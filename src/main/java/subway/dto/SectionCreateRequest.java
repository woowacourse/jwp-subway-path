package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionCreateRequest {
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String upStation;
    @NotBlank(message = "역 이름은 공백일 수 없습니다.")
    private String downStation;
    @NotNull(message = "거리는 필수 입력값입니다.")
    @Positive(message = "거리는 양수여야 합니다.")
    private Integer distance;
    @NotNull(message = "노선 아이디는 필수 입력값입니다.")
    @Positive(message = "노선 아이디는 양수여야 합니다.")
    private Long lineId;

    public SectionCreateRequest() {

    }

    public SectionCreateRequest(String upStation, String downStation, int distance, long lineId) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.lineId = lineId;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
