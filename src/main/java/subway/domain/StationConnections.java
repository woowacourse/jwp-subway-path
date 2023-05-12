package subway.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class StationConnections {

    private final Map<Station, Connection> connectionsByStation;

    private StationConnections(final Map<Station, Connection> connectionsByStation) {
        this.connectionsByStation = connectionsByStation;
    }

    public static StationConnections from(List<Section> sections) {
        Map<Station, Connection> connectionByStation = generateConnectionByStation(sections);
        return new StationConnections(connectionByStation);
    }

    private static Map<Station, Connection> generateConnectionByStation(final List<Section> sections) {
        Map<Station, Connection> connectionByStation = new HashMap<>();
        for (Section section : sections) {
            Station left = section.getLeft();
            Station right = section.getRight();
            updateLeftConnection(connectionByStation, left);
            updateRightConnection(connectionByStation, right);
        }
        validateDisconnection(connectionByStation);
        return connectionByStation;
    }

    private static void validateDisconnection(final Map<Station, Connection> connectionByStation) {
        int disconnectedCount = (int) connectionByStation.entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .filter(Connection::isDisConnected)
                .count();
        if (!connectionByStation.isEmpty() && disconnectedCount != 2) {
            throw new IllegalArgumentException("구간은 하나의 연결로 이루어져 있어야 합니다.");
        }
    }

    private static void updateLeftConnection(final Map<Station, Connection> connectionByStation, final Station left) {
        validateDuplicatedConnection(connectionByStation, left);
        if (!connectionByStation.containsKey(left)) {
            connectionByStation.put(left, new Connection(false, true));
            return;
        }
        connectionByStation.put(left, new Connection(true, true));
    }

    private static void validateDuplicatedConnection(final Map<Station, Connection> connectionByStation,
                                                     final Station station) {
        if (connectionByStation.containsKey(station) && isExists(connectionByStation, station)) {
            throw new IllegalArgumentException("구간을 중복으로 저장할 수 없습니다.");
        }
    }

    private static boolean isExists(final Map<Station, Connection> connectionByStation, final Station station) {
        return connectionByStation.get(station).isLeft() && connectionByStation.get(station).isRight();
    }

    private static void updateRightConnection(final Map<Station, Connection> connectionByStation, final Station right) {
        validateDuplicatedConnection(connectionByStation, right);
        if (!connectionByStation.containsKey(right)) {
            connectionByStation.put(right, new Connection(true, false));
            return;
        }
        connectionByStation.put(right, new Connection(true, true));
    }

    public Station findStartStation() {
        return connectionsByStation.entrySet()
                .stream()
                .filter(entry -> isStart(entry.getValue()))
                .map(Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public Station findEndStation() {
        return connectionsByStation.entrySet()
                .stream()
                .filter(entry -> isEnd(entry.getValue()))
                .map(Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private boolean isStart(Connection connection) {
        return (!connection.isLeft()) && (connection.isRight());
    }

    private boolean isEnd(Connection connection) {
        return (connection.isLeft()) && (!connection.isRight());
    }

    public Set<Station> getAllStations() {
        return connectionsByStation.keySet();
    }

    public Map<Station, Connection> getConnectionsByStation() {
        return connectionsByStation;
    }
}
