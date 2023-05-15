package subway.ui.dto;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "등록할 역의 이름이 있어야 합니다.")
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
