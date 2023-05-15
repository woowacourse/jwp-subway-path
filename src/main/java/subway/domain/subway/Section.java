package subway.domain.subway;

import subway.domain.common.Distance;
import subway.exception.SectionForkedException;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(final Station upStation, final Station downStation, final long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = new Distance(distance);
    }

    public boolean hasStation(final Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    public void validateForkedSection(final long requestDistance) {
        if (distance.isShorterOrEqualThan(requestDistance)) {
            throw new SectionForkedException();
        }
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public long getDistance() {
        return distance.getDistance();
    }
}
