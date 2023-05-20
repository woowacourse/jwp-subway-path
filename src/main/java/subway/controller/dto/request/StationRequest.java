package subway.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class StationRequest {

    @NotBlank(message = "name 이 비어있습니다.")
    private final String name;

    @JsonCreator
    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
