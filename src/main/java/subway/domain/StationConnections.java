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
        return connectionByStation;
    }

    private static void updateLeftConnection(final Map<Station, Connection> connectionByStation, final Station left) {
        if (!connectionByStation.containsKey(left)) {
            connectionByStation.put(left, new Connection(false, true));
            return;
        }
        connectionByStation.put(left, new Connection(true, true));
    }

    private static void updateRightConnection(final Map<Station, Connection> connectionByStation, final Station right) {
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
                .orElseThrow(() -> new IllegalStateException("시작점을 찾을 수 없습니다."));
    }

    public Station findEndStation() {
        return connectionsByStation.entrySet()
                .stream()
                .filter(entry -> isEnd(entry.getValue()))
                .map(Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("종점을 찾을 수 없습니다."));
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
