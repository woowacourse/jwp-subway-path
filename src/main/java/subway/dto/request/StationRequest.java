package subway.dto.request;

import javax.validation.constraints.NotNull;

public class StationRequest {

    @NotNull(message = "이름은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
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
