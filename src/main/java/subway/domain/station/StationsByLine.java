package subway.domain.station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import subway.domain.line.Line;
import subway.exception.StationNotFoundException;

public class StationsByLine {

    private final Map<Line, Stations> stationsByLine;

    private StationsByLine(Map<Line, Stations> stationsByLine) {
        this.stationsByLine = new HashMap<>(stationsByLine);
    }

    public static StationsByLine of(List<Line> lines, List<Station> stations) {
        Map<Line, Stations> stationsByLine = new HashMap<>();
        for (Line line : lines) {
            initStationsByLine(stations, stationsByLine, line);
        }
        return new StationsByLine(stationsByLine);
    }

    private static void initStationsByLine(List<Station> stations, Map<Line, Stations> stationsByLine, Line line) {
        List<Station> sameLineStations = stations.stream()
                .filter(station -> station.isSameLine(line))
                .collect(Collectors.toUnmodifiableList());
        stationsByLine.put(line,  new Stations(sameLineStations));
    }

    public Station findStationByStationNameAndLine(String stationName, Line line) {
        Stations stations = stationsByLine.get(line);
        return stations.findStationByStationName(stationName)
                .orElseThrow(() -> new IllegalArgumentException("역 이름에 해당하는 역을 찾을 수 없습니다."));
    }

    public StationDirection determineStationDirectionBy(String upStationName, String downStationName, Line line) {
        Stations stations = stationsByLine.get(line);
        Optional<Station> findUpStation = stations.findStationByStationName(upStationName);
        Optional<Station> findDownStation = stations.findStationByStationName(downStationName);

        validateStationStatus(findUpStation, findDownStation);

        if (findUpStation.isEmpty()) {
            return StationDirection.UP;
        }
        return StationDirection.DOWN;
    }

    private void validateStationStatus(Optional<Station> requestUpStation, Optional<Station> requestDownStation) {
        if (requestUpStation.isEmpty() && requestDownStation.isEmpty()) {
            throw new StationNotFoundException("두개의 역 모두가 존재하지 않습니다.");
        }

        if (requestUpStation.isPresent() && requestDownStation.isPresent()) {
            throw new IllegalArgumentException("두개의 역이 이미 모두 존재합니다.");
        }
    }
}
