package subway.service.section.domain;

import subway.persistence.dao.entity.SectionEntity;
import subway.persistence.dao.entity.StationEntity;
import subway.service.station.domain.Station;

import java.util.Map;
import java.util.Objects;

public class Section {
    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Long id, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, Distance distance) {
        this(null, upStation, downStation, distance);
    }

    public static Section of(SectionEntity sectionEntity, Map<Long, StationEntity> stationEntityMap) {
        StationEntity upStationEntity = stationEntityMap.get(sectionEntity.getUpStationId());
        Station upStation = new Station(upStationEntity.getId(), upStationEntity.getName());

        StationEntity downStationEntity = stationEntityMap.get(sectionEntity.getDownStationId());
        Station downStation = new Station(downStationEntity.getId(), downStationEntity.getName());
        return new Section(
                sectionEntity.getId(),
                upStation,
                downStation,
                new Distance(sectionEntity.getDistance())
        );
    }

    public boolean contains(Station station) {
        return upStation.equals(station) || downStation.equals(station);
    }

    public boolean isDownStation(Station station) {
        return downStation.equals(station);
    }

    public boolean isUpStation(Station station) {
        return upStation.equals(station);
    }

    public Distance calculateNewSectionDistance(Distance another) {
        return this.distance.reduce(another);
    }

    public Distance calcuateCombineDistance(Section another) {
        return this.distance.plus(another.distance);
    }

    public boolean isSmaller(Distance another) {
        return distance.isSmaller(another);
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }


    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance);
    }
}
