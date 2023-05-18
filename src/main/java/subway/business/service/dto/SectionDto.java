package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import subway.business.domain.line.Section;

public class SectionDto {
    @Schema(description = "구간의 ID")
    private final Long id;

    @Schema(description = "구간의 상행 방향 역")
    private final StationDto upwardStation;

    @Schema(description = "구간의 하행 방향 역 이름")
    private final StationDto downwardStation;

    @Schema(description = "구간의 거리")
    private final int distance;

    private SectionDto(Long id, StationDto upwardStation, StationDto downwardStation, int distance) {
        this.id = id;
        this.upwardStation = upwardStation;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public static SectionDto from(Section section) {
        return new SectionDto(
                section.getId(),
                StationDto.from(section.getUpwardStation()),
                StationDto.from(section.getDownwardStation()),
                section.getDistance()
        );
    }

    public Long getId() {
        return id;
    }

    public StationDto getUpwardStation() {
        return upwardStation;
    }

    public StationDto getDownwardStation() {
        return downwardStation;
    }

    public int getDistance() {
        return distance;
    }
}
