package subway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class StationCreateRequest {

    @NotNull(message = "역의 이름을 입력해 주세요. 입력값: ${validatedValue}")
    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
