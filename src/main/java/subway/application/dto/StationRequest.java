package subway.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.constraints.NotNull;

public class StationRequest {

    @NotNull
    private String name;

    @JsonCreator
    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
