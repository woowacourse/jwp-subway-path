package subway.dto.section;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class SectionCreateRequest {
    @NotBlank(message = "상행역은 비어있을 수 없습니다.")
    @Length(
            max = 10,
            message = "역의 이름은 {max}글자를 초과할 수 없습니다."
    )
    private String upBoundStationName;

    @NotBlank(message = "하행역은 비어있을 수 없습니다.")
    @Length(
            max = 10,
            message = "역의 이름은 {max}글자를 초과할 수 없습니다."
    )
    private String downBoundStationName;

    @Range(
            min = 1,
            max = 100,
            message = "구간의 길이는 {min}~{max} 사이여야 합니다."
    )
    private int distance;

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
