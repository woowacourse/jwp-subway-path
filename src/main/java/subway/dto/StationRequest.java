package subway.dto;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "역 이름을 입력해 주세요.")
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
