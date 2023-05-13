package subway.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import subway.domain.station.Station;

public class StationResponse {

    @Schema(description = "역 ID")
    private Long id;

    @Schema(description = "역 이름")
    private String name;

    public StationResponse() {

    }

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
