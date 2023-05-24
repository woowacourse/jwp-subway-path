package subway.application.strategy.insert;

import subway.domain.Distance;
import subway.domain.Station;

public class InsertSection {

    private final Station upStation;
    private final Station downStation;
    private final Distance distance;
    private final Long lineId;

    public InsertSection(Station upStation, Station downStation, int distance, Long lineId) {
        this(upStation, downStation, Distance.from(distance), lineId);
    }

    public InsertSection(Station upStation, Station downStation, Distance distance, Long lineId) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.lineId = lineId;
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

    public Long getLineId() {
        return lineId;
    }
}
