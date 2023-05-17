package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import subway.business.domain.Section;

public class SectionDto {
    @Schema(description = "구간의 ID")
    private final Long id;

    @Schema(description = "구간의 상행 방향 역 이름")
    private final String upwardStation;

    @Schema(description = "구간의 하행 방향 역 이름")
    private final String downwardStation;

    @Schema(description = "구간의 거리")
    private final int distance;

    private SectionDto(Long id, String upwardStation, String downwardStation, int distance) {
        this.id = id;
        this.upwardStation = upwardStation;
        this.downwardStation = downwardStation;
        this.distance = distance;
    }

    public static SectionDto from(Section section) {
        return new SectionDto(
                section.getId(),
                section.getUpwardStation().getName(),
                section.getDownwardStation().getName(),
                section.getDistance()
        );
    }

    public Long getId() {
        return id;
    }

    public String getUpwardStation() {
        return upwardStation;
    }

    public String getDownwardStation() {
        return downwardStation;
    }

    public int getDistance() {
        return distance;
    }
}
