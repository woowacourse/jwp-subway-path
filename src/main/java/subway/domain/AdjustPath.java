package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjustPath {

    private static final String NOT_CONNECTED_STATION_MESSAGE = "인접하지 않은 역 입니다.";
    private static final int END_STATION_PATH_SIZE = 1;

    private final Map<Station, PathInfo> path;

    private AdjustPath(final Map<Station, PathInfo> path) {
        this.path = path;
    }

    public static AdjustPath create() {
        return new AdjustPath(new HashMap<>());
    }

    public void add(final Station station, final Distance distance, final Direction status) {
        path.put(station, PathInfo.of(distance, status));
    }

    public void delete(final Station station) {
        validateConnectStation(station);

        path.remove(station);
    }

    public PathInfo findPathInfoByStation(final Station station) {
        validateConnectStation(station);

        return path.get(station);
    }

    public Direction findEndStationPathDirection() {
        final Collection<PathInfo> pathInfos = path.values();

        if (pathInfos.size() != 1) {
            throw new IllegalArgumentException("해당 역은 종점역이 아닙니다.");
        }

        return pathInfos.stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 노선에 등록된 역이 없습니다."))
                .getDirection();
    }

    private void validateConnectStation(final Station station) {
        if (!isConnect(station)) {
            throw new IllegalArgumentException(NOT_CONNECTED_STATION_MESSAGE);
        }
    }

    public boolean isEnd() {
        return path.keySet().size() == END_STATION_PATH_SIZE;
    }

    public boolean isConnect(final Station station) {
        return path.containsKey(station);
    }

    public Station findStationByDirection(final Direction direction) {
        return path.keySet()
                .stream()
                .filter(station -> path.get(station).matchesByDirection(direction))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NOT_CONNECTED_STATION_MESSAGE));
    }

    public List<Station> findAllStation() {
        return new ArrayList<>(path.keySet());
    }
}
