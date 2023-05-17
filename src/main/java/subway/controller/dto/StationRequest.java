package subway.controller.dto;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "역 이름은 빈 값이 될 수 없습니다.")
    private String name;

    private StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
