package subway.dto;

import javax.validation.constraints.NotBlank;

public class StationRequest {
    @NotBlank(message = "역 이름이 필요합니다.")
    private String name;

    public StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
