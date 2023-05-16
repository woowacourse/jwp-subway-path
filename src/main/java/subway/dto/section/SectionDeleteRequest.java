package subway.dto.section;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class SectionDeleteRequest {
    @NotBlank(message = "역 이름은 비어있을 수 없습니다.")
    @Length(
            max = 10,
            message = "역의 이름은 {max}글자를 초과할 수 없습니다."
    )
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
