package subway.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreationStationRequest {

    private final String name;

    @JsonCreator
    public CreationStationRequest(@JsonProperty(value = "name") final String name) {
        this.name = name;
    }

    public static CreationStationRequest from(final String name) {
        return new CreationStationRequest(name);
    }

    public String getName() {
        return name;
    }
}
