package subway.domain.subwayMap.domain;

import org.springframework.stereotype.Component;
import subway.domain.lineDetail.domain.LineDetail;
import subway.domain.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SubwayMap {

    private final Map<LineDetail, List<Station>> subwayMaps;

    public SubwayMap() {
        this.subwayMaps = new ConcurrentHashMap<>();
    }

    public void put(final LineDetail lineDetail, final List<Station> sections) {
        subwayMaps.put(lineDetail, new ArrayList<>(sections));
    }

    public List<Station> getSubwayMapByLine(final LineDetail lineDetail) {
        return subwayMaps.getOrDefault(lineDetail, new ArrayList<>());
    }

    public void addStation(LineDetail lineDetail, Station baseStationId, Station addStationId, Boolean direction) {
        List<Station> stations = subwayMaps.get(lineDetail);
        if (direction) {
            int baseStationIndex = stations.indexOf(baseStationId);
            int UpStation = 1;
            stations.add(baseStationIndex + UpStation, addStationId);
            return;
        }
        int index = stations.indexOf(baseStationId);
        stations.add(index, addStationId);
    }

    public void deleteStation(LineDetail lineDetail, Station station) {
        List<Station> stations = subwayMaps.get(lineDetail);

        stations.remove(station);
    }
}
