package subway.domain;

import subway.dao.dto.SectionStationResultMap;

import java.util.Objects;

public class Section {
    private Long id;
    private int distance;
    private Station upStation;
    private Station downStation;
    private Long lineId;

    public Section(int distance, Station upStation, Station downStation, Long lineId) {
        this(null, distance, upStation, downStation, lineId);
    }

    public Section(Long id, int distance, Station upStation, Station downStation, Long lineId) {
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
                sectionStationResultMap.getDistance(),
                new Station(sectionStationResultMap.getUpStationId(), sectionStationResultMap.getUpStationName()),
                new Station(sectionStationResultMap.getDownStationId(), sectionStationResultMap.getDownStationName()),
                sectionStationResultMap.getLineId());
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
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
}
