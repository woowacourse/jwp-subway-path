package subway.dto;

import javax.validation.constraints.NotNull;

public class StationRequest {

    @NotNull
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
