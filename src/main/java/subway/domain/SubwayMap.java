package subway.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubwayMap {

    private final Map<Long, Line> lineByLineId;
    private final Map<Long, List<Station>> stationsByLineId;

    private SubwayMap(final Map<Long, Line> lineByLineId, final Map<Long, List<Station>> stationsByLineId) {
        this.lineByLineId = lineByLineId;
        this.stationsByLineId = stationsByLineId;
    }

    public static SubwayMap of(final List<Line> lines, final List<Section> sections) {
        final SubwayMap subwayMap = new SubwayMap(new HashMap<>(), new HashMap<>());
        final StationGraph stationGraph = StationGraph.of(sections);

        subwayMap.addLines(lines);
        subwayMap.createAllStations(stationGraph, sections);
        return subwayMap;
    }

    private void addLines(final List<Line> lines) {
        lines.forEach(line -> lineByLineId.put(line.getId(), line));
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

    public Line getLine(final Long lineId) {
        final Line line = lineByLineId.get(lineId);
        if (line == null) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다.");
        }
        return line;
    }

    public Set<Long> getAllLineIds() {
        final Set<Long> lineIds = lineByLineId.keySet();
        if (lineIds.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다.");
        }
        return lineIds;
    }

    public List<Station> getStations(final Long lineId) {
        final List<Station> stations = stationsByLineId.get(lineId);
        if (stations == null) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다.");
        }
        return stations;
    }
}
