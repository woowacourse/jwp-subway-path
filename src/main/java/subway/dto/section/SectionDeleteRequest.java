package subway.dto.section;

import org.hibernate.validator.constraints.Length;
import subway.validation.annotation.EndsWith;

public class SectionDeleteRequest {
    @Length(
            min = 2,
            max = 10,
            message = "역의 이름은 {min}글자 이상, {max}글자를 미만이어야 합니다."
    )
    @EndsWith(suffix ="역", message = "역의 이름은 '{suffix}'으로 끝나야 합니다.")
    private String stationName;

    public SectionDeleteRequest() {
    }

    public SectionDeleteRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
