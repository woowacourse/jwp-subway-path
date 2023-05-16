package subway.subwayMap.domain;

import org.springframework.stereotype.Component;
import subway.line.domain.Line;
import subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SubwayMap {

    private final Map<Line, List<Station>> subwayMaps;

    public SubwayMap() {
        this.subwayMaps = new ConcurrentHashMap<>();
    }

    public void put(final Line line, final List<Station> sections) {
        subwayMaps.put(line, new ArrayList<>(sections));
    }

    public List<Station> getSubwayMapByLine(final Line line) {
        return subwayMaps.getOrDefault(line, new ArrayList<>());
    }

    public void addStation(Line line, Station baseStationId, Station addStationId, Boolean direction) {
        List<Station> stations = subwayMaps.get(line);
        if (direction) {
            int baseStationIndex = stations.indexOf(baseStationId);
            int UpStation = 1;
            stations.add(baseStationIndex + UpStation, addStationId);
            return;
        }
        int index = stations.indexOf(baseStationId);
        stations.add(index, addStationId);
    }

    public void deleteStation(Line line, Station station) {
        List<Station> stations = subwayMaps.get(line);

        stations.remove(station);
    }
}
