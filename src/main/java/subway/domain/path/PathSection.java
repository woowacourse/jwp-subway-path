package subway.domain.path;

import subway.domain.section.Distance;
import subway.domain.station.Station;

public class PathSection {

    private final Station sourceStation;
    private final Station targetStation;
    private final Distance distance;

    private PathSection(final Station sourceStation, final Station targetStation, final Distance distance) {
        validateStations(sourceStation, targetStation);

        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    private void validateStations(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("동일한 역은 이동할 수 없습니다.");
        }
    }

    public static PathSection of(final Station sourceStation, final Station targetStation, final Distance distance) {
        return new PathSection(sourceStation, targetStation, distance);
    }

    public int distance() {
        return distance.distance();
    }

    public Station sourceStation() {
        return sourceStation;
    }

    public Station targetStation() {
        return targetStation;
    }
}
