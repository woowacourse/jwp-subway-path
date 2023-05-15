package subway.domain.section;

import subway.domain.station.Station;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(final Station upStation, final Station downStation, final Distance distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(final Long id, final Station upStation, final Station downStation, final Distance distance) {
        validateStation(upStation);
        validateStation(downStation);
        validateDifferentStation(upStation, downStation);
        validateDistance(distance);

        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }


    private void validateStation(final Station station) {
        if (station == null) {
            throw new IllegalArgumentException("구간에 역은 필수로 존재해야 합니다.");
        }
    }

    private void validateDifferentStation(final Station upStation, final Station downStation) {
        if (upStation.isSameStation(downStation)) {
            throw new IllegalArgumentException("한 구간은 서로 다른 역으로 이루어져야 합니다.");
        }
    }

    private void validateDistance(final Distance distance) {
        if (distance == null) {
            throw new IllegalArgumentException("구간은 역 사이에 거리가 필수로 존재해야합니다.");
        }
    }
}
