package subway.domain;

import java.util.HashMap;
import java.util.Map;

public class AdjustPath {

    private final Map<Station, Distance> adjustPath;

    private AdjustPath(final Map<Station, Distance> adjustPath) {
        this.adjustPath = adjustPath;
    }

    public static AdjustPath create() {
        return new AdjustPath(new HashMap<>());
    }

    public void add(final Station station, final Distance distance) {
        adjustPath.put(station, distance);
    }

    public Distance findDistance(final Station station) {
        validateExistStation(station);

        return adjustPath.get(station);
    }

    private void validateExistStation(final Station station) {
        if (!adjustPath.containsKey(station)) {
            throw new IllegalArgumentException("인접하지 않은 역 입니다");
        }
    }

    public void delete(final Station station) {
        validateExistStation(station);

        adjustPath.remove(station);
    }
}
