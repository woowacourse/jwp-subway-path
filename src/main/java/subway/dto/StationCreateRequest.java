package subway.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class StationCreateRequest {

    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
