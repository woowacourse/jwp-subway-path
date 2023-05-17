package subway.controller.dto;

import javax.validation.constraints.NotBlank;

public class SectionDeleteRequest {

    @NotBlank(message = "역 이름은 빈 값이 될 수 없습니다.")
    private String stationName;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
