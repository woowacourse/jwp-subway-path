package subway.presentation.request;

import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

    @NotBlank(message = "역을 입력해주세요.")
    private String name;

    private StationCreateRequest() {
    }

    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
