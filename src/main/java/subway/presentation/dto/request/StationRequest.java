package subway.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class StationRequest {

    @NotNull(message = "name 이 null 이면 안됩니다.")
    private final String name;

    @JsonCreator
    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
