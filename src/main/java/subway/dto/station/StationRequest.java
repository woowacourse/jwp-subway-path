package subway.dto.station;

import javax.validation.constraints.NotBlank;

public class StationRequest {
    @NotBlank
    private String name;

    private StationRequest() {
    }

    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
