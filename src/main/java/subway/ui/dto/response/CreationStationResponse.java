package subway.ui.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import subway.domain.Station;

public class CreationStationResponse {

    private Long id;
    private String name;

    @JsonCreator
    public CreationStationResponse(@JsonProperty(value = "id") final Long id,
                                   @JsonProperty(value = "name") final String name) {
        this.id = id;
        this.name = name;
    }

    public static CreationStationResponse from(final Station station) {
        return new CreationStationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
