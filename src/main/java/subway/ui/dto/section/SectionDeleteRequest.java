package subway.ui.dto.section;

import com.fasterxml.jackson.annotation.JsonCreator;
import javax.validation.constraints.NotBlank;
import subway.application.dto.section.SectionDeleteDto;

public class SectionDeleteRequest {
    @NotBlank(message = "역 이름은 비어있을 수 없습니다.")
    private String stationName;

    @JsonCreator
    public SectionDeleteRequest(String stationName) {
        this.stationName = stationName;
    }

    public SectionDeleteDto toSectionDeleteDto(long id) {
        return new SectionDeleteDto(id, stationName);
    }

    public String getStationName() {
        return stationName;
    }
}
