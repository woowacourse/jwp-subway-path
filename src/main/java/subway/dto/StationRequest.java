package subway.dto;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "역 이름은 필수로 입력해야 합니다.")
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
