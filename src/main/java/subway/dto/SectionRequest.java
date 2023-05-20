package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class SectionRequest {
    @NotBlank(message = "상행역 이름은 빈 문자열일 수 없습니다.")
    private String upStationName;

    @NotBlank(message = "하행역 이름은 빈 문자열일 수 없습니다.")
    private String downStationName;

    @Positive(message = "역 간 거리는 1 이상이어야 합니다.")
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(final String upStationName, final String downStationName, final int distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }
}
