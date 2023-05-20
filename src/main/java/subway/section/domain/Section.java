package subway.section.domain;

import subway.section.domain.entity.SectionEntity;
import subway.station.domain.Station;
import subway.vo.Distance;

public class Section {

    private final Long lineId;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    private Section(final Long lineId, final Station upStation, final Station downStation, final Distance distance) {
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(final Long id, final Station upStation, final Station downStation, final int distance) {
        return new Section(id, upStation, downStation, Distance.from(distance));
    }

    public static Section of(final Long id, final Long upStationId, final Long downStationId, final int distance) {
        return new Section(id, Station.from(upStationId), Station.from(downStationId), Distance.from(distance));
    }

    public static Section from(final SectionEntity sectionEntity) {
        return new Section(
                sectionEntity.getId(),
                Station.from(sectionEntity.getUpStationId()),
                Station.from(sectionEntity.getDownStationId()),
                Distance.from(sectionEntity.getDistance())
        );
    }

    public SectionEntity toEntity() {
        return SectionEntity.of(lineId, upStation.getId(), downStation.getId(), distance.getValue());
    }

    public Long getLineId() {
        return lineId;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistanceValue() {
        return distance.getValue();
    }

    public boolean upStationIdIsSameId(final Long beforeStationId) {
        return upStation.getId() == beforeStationId;
    }

    public Section getLeftSection(final Long lineId, final Long downStationId, final int distance) {
        return Section.of(lineId, upStation.getId(), downStationId, distance);
    }

    public Section getRightSection(final Long lineId, final Long upStationId, final int distance) {
        return Section.of(lineId, upStationId, downStation.getId(), this.distance.getValue() - distance);
    }

}
