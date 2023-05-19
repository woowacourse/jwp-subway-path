package subway.dto.section;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import subway.validation.annotation.EndsWith;

public class SectionCreateRequest {
    @Length(
            min = 2,
            max = 10,
            message = "역의 이름은 {min}글자 이상, {max}글자를 미만이어야 합니다."
    )
    @EndsWith(suffix ="역", message = "역의 이름은 '{suffix}'으로 끝나야 합니다.")
    private String upBoundStationName;

    @Length(
            min = 2,
            max = 10,
            message = "역의 이름은 {min}글자 이상, {max}글자를 미만이어야 합니다."
    )
    @EndsWith(suffix ="역", message = "역의 이름은 '{suffix}'으로 끝나야 합니다.")
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
