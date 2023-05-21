package subway.domain;

import java.util.Objects;
import java.util.Optional;

public class Section {
    private final Long id;

    private final Long lineId;

    private final Station upStation;

    private final Station downStation;

    private final Distance distance;

    public Section(Long id, Long lineId, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Station upStation, Station downStation, Distance distance, long lineId) {
        this(null, lineId, upStation, downStation, distance);
    }

//    public static  Section from(Station upStation, Station downStation, Distance distance) {
//        return new Section (null, null, upStation, downStation, distance);
//    }

    public Optional<Station> getUpOrDownStation(long baseStationId) {
        if (upStation.isEqualsTo(baseStationId)) {
            return Optional.of(upStation);
        }

        if (downStation.isEqualsTo(baseStationId)) {
            return Optional.of(downStation);
        }
        return Optional.empty();
    }

    public boolean hasUpStation(Station upStation) {
        return this.upStation.equals(upStation);
    }

    public boolean hasDownStation(Station downStation) {
        return this.downStation.equals(downStation);
    }


    public Distance subtract(Section other) {
        return distance.subtract(other.distance);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }

    // TODO: 지우기
    public long getId() {
        return id;
    }
}
