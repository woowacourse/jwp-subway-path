package subway.dto;

import subway.domain.section.Section;

import java.util.Objects;

public class SectionDto {

    private final Long id;
    private final String upStationName;
    private final String downStationName;
    private final int distance;

    private SectionDto(Long id, String upStationName,
                       String downStationName, int distance) {
        this.id = id;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public static SectionDto from(Section section) {
        return new SectionDto(section.getId(), section.getUpStation().getName(),
                section.getDownStation().getName(), section.getDistanceValue());
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionDto that = (SectionDto) o;
        return distance == that.distance && Objects.equals(id, that.id) && Objects.equals(upStationName, that.upStationName) && Objects.equals(downStationName, that.downStationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStationName, downStationName, distance);
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
