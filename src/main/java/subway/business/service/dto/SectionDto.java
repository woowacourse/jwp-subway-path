package subway.business.service.dto;

import subway.business.domain.Section;

public class SectionDto {
    private final Long id;
    private final String upwardStation;
    private final String downwardStation;
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
