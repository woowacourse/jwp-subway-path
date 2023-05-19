package subway.ui.station.dto.in;

import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

    @NotBlank
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