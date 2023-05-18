package subway.ui.dto.request;

import javax.validation.constraints.NotBlank;

public class CreationStationRequest {

    @NotBlank
    private final String name;

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
