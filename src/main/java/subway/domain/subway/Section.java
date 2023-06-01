package subway.domain.subway;

import subway.dao.dto.SectionStationResultMap;

import java.util.Objects;

public class Section {
    private Long id;
    private Distance distance;
    private Station upStation;
    private Station downStation;
    private Long lineId;

    public Section(Distance distance, Station upStation, Station downStation, Long lineId) {
        this(null, distance, upStation, downStation, lineId);
    }

    public Section(Long id, Distance distance, Station upStation, Station downStation, Long lineId) {
        validateDuplicate(upStation, downStation);
        this.id = id;
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
        this.lineId = lineId;
    }

    private void validateDuplicate(Station upStation, Station downStation) {
        if (Objects.equals(upStation.getId(), downStation.getId())) {
            throw new IllegalArgumentException("같은 역을 구간으로 등록할 수 없습니다.");
        }
    }

    public static Section of(final SectionStationResultMap sectionStationResultMap) {
        return new Section(
                sectionStationResultMap.getSectionId(),
                new Distance(sectionStationResultMap.getDistance()),
                new Station(sectionStationResultMap.getUpStationId(), sectionStationResultMap.getUpStationName()),
                new Station(sectionStationResultMap.getDownStationId(), sectionStationResultMap.getDownStationName()),
                sectionStationResultMap.getLineId());
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getUpStationId() {
        return upStation.getId();
    }

    public Long getDownStationId() {
        return downStation.getId();
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(lineId, section.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance, upStation, downStation, lineId);
    }
}
