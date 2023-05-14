package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class StationRequest {
    @NotBlank(message = "역 이름을 입력해야 합니다.")
    private String name;

    private StationRequest() {
    }

    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
