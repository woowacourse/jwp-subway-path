package subway.domain.section;

import subway.domain.Distance;
import subway.domain.Station;

public class Section {
    private final Long id;
    private final Distance distance;
    private final Station upStation;
    private final Station downStation;
    private final Long lineId;

    public Section(int distance, Station upStation, Station downStation, Long lineId) {
        this(null, Distance.from(distance), upStation, downStation, lineId);
    }

    public Section(Distance distance, Station upStation, Station downStation, Long lineId) {
        this(null, distance, upStation, downStation, lineId);
    }

    public Section(Long id, Distance distance, Station upStation, Station downStation, Long lineId) {
        this.id = id;
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
        this.lineId = lineId;
    }

    public boolean hasSameSection(Station upStation, Station downStation) {
        return this.upStation.equals(upStation) && this.downStation.equals(downStation) ;
    }

    public Long getId() {
        return id;
    }

    public Distance getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getLineId() {
        return lineId;
    }
}
