package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubwayMap {

    private final Map<Long, List<Station>> stationsByLineId;

    private SubwayMap(final Map<Long, List<Station>> stationsByLineId) {
        this.stationsByLineId = stationsByLineId;
    }

    public static SubwayMap of(final List<Section> sections) {
        final SubwayMap subwayMap = new SubwayMap(new HashMap<>());
        final StationGraph stationGraph = StationGraph.of(sections);

        subwayMap.createAllStations(stationGraph, sections);
        return subwayMap;
    }

    private void createAllStations(final StationGraph stationGraph, final List<Section> sections) {
        final Set<Long> lineIds = new HashSet<>();

        for (final Section section : sections) {
            final Long lineId = section.getLineId();
            if (lineIds.contains(lineId)) {
                continue;
            }
            lineIds.add(lineId);
            final List<Station> stations = stationGraph.findStations(section);
            stationsByLineId.put(lineId, stations);
        }
    }

    public List<Station> getStations(final Long lineId) {
        return stationsByLineId.get(lineId);
    }

    public List<List<Station>> getAllStations() {
        return new ArrayList<>(stationsByLineId.values());
    }
}
