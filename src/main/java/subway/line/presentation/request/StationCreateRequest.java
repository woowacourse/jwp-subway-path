package subway.line.presentation.request;

import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

    @NotBlank(message = "역 이름을 입력해야 합니다")
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
