package subway.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotBlank;

public class CreationStationRequest {

    @NotBlank
    private final String name;

    @JsonCreator
    private CreationStationRequest(final String name) {
        this.name = name;
    }

    public static CreationStationRequest from(final String name) {
        return new CreationStationRequest(name);
    }

    public String getName() {
        return name;
    }
}
