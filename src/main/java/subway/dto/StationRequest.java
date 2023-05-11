package subway.dto;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "역 이름이 입력되지 않았습니다.")
    private String name;

    public StationRequest() {
        this(null);
    }

    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
