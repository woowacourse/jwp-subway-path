package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StationDto {
    @Schema(description = "역의 ID")
    private final Long id;

    @Schema(description = "역의 이름")
    private final String name;

    public StationDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
