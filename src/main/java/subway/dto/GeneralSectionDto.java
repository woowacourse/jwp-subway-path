package subway.dto;

import subway.domain.section.general.GeneralSection;

public class GeneralSectionDto {

    private final Long id;
    private final String upStationName;
    private final String downStationName;
    private final int distance;

    private GeneralSectionDto(Long id, String upStationName,
                              String downStationName, int distance) {
        this.id = id;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public static GeneralSectionDto from(GeneralSection section) {
        return new GeneralSectionDto(section.getId(), section.getUpStation().getName(),
                section.getDownStation().getName(), section.getDistance());
    }

    public Long getId() {
        return id;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "SectionDto{" +
                "id=" + id +
                ", upStationName='" + upStationName + '\'' +
                ", downStationName='" + downStationName + '\'' +
                ", distance=" + distance +
                '}';
    }
}
