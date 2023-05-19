package subway.domain;

import subway.dao.vo.SectionStationMapper;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Integer distance;

    private Section(Long id, Station upStation, Station downStation, Integer distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section from(SectionStationMapper sectionStationMapper) {
        return new Section(
                sectionStationMapper.getId(),
                Station.of(sectionStationMapper.getUpStationId(), sectionStationMapper.getUpStationName()),
                Station.of(sectionStationMapper.getDownStationId(), sectionStationMapper.getDownStationName()),
                sectionStationMapper.getDistance()
        );
    }

    public static Section of(Long id, Station upStation, Station downStation, Integer distance) {
        return new Section(id, upStation, downStation, distance);
    }

    public Long getId() {
        return id;
    }

    public boolean isUpStation(Station station) {
        return this.upStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
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
        Section section = (Section) o;
        return Objects.equals(upStation, section.upStation) && Objects.equals(downStation,
                section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }
}
