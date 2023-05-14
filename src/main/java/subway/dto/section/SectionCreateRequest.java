package subway.dto.section;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class SectionCreateRequest {
    @NotBlank(message = "상행역은 비어있을 수 없습니다.")
    private String upBoundStationName;

    @NotBlank(message = "하행역은 비어있을 수 없습니다.")
    private String downBoundStationName;

    @Positive(message = "구간의 길이는 양수여야 합니다.")
    private int distance;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(String upBoundStationName, String downBoundStationName, int distance) {
        this.upBoundStationName = upBoundStationName;
        this.downBoundStationName = downBoundStationName;
        this.distance = distance;
    }

    public String getUpBoundStationName() {
        return upBoundStationName;
    }

    public String getDownBoundStationName() {
        return downBoundStationName;
    }

    public int getDistance() {
        return distance;
    }
}
