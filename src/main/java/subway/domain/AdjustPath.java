package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjustPath {

    private final Map<Station, PathInfo> adjustPath;

    private AdjustPath(final Map<Station, PathInfo> adjustPath) {
        this.adjustPath = adjustPath;
    }

    public static AdjustPath create() {
        return new AdjustPath(new HashMap<>());
    }

    public void add(final Station station, final Distance distance, final RelationStatus status) {
        adjustPath.put(station, PathInfo.of(distance, status));
    }

    public PathInfo findDistance(final Station station) {
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

    public PathInfo findPathInfoByStation(final Station station) {
        validateExistStation(station);

        return adjustPath.get(station);
    }

    public boolean contains(final Station station) {
        return adjustPath.containsKey(station);
    }

    public List<Station> findAllStation() {
        return new ArrayList<>(adjustPath.keySet());
    }

    public Station findUpStation() {
        return adjustPath.keySet()
                .stream()
                .filter(station -> adjustPath.get(station).isUpStation())
                .findAny()
                .orElseThrow();
    }

    public Station findDownStation() {
        return adjustPath.keySet()
                .stream()
                .filter(station -> !adjustPath.get(station).isUpStation())
                .findAny()
                .orElseThrow();
    }
}
