package subway.ui.dto.section;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class SectionCreateRequest {

    @NotBlank(message = "상행역은 비어있을 수 없습니다.")
    private String startStationName;

    @NotBlank(message = "하행역은 비어있을 수 없습니다.")
    private String endStationName;

    @Positive(message = "구간의 길이는 양수여야 합니다.")
    private int distance;

    @JsonCreator
    public SectionCreateRequest(String startStationName, String endStationName, int distance) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public int getDistance() {
        return distance;
    }
}
