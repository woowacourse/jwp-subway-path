package subway.station.dto.request;

import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

    @NotBlank
    private String name;

    private StationCreateRequest() {
    }

    public StationCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
