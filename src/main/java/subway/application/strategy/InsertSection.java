package subway.application.strategy;

import subway.domain.Distance;
import subway.domain.Station;

public class InsertSection {

    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public InsertSection(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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
}
