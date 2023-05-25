package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import subway.business.domain.line.Station;

public class StationDto {
    @Schema(description = "역의 ID")
    private final Long id;

    @Schema(description = "역의 이름")
    private final String name;

    private StationDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationDto from(Station station) {
        return new StationDto(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
