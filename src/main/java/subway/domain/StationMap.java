package subway.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StationMap {

    private final Lines lines;
    private final Map<Line, Stations> stationsByLine;

    private StationMap(final Lines lines, final Map<Line, Stations> stationsByLine) {
        this.lines = lines;
        this.stationsByLine = stationsByLine;
    }

    public static StationMap of(final List<Line> lines, final List<Section> sections) {
        final StationMap stationMap = new StationMap(Lines.of(lines), new HashMap<>());
        final StationGraph stationGraph = StationGraph.of(sections);

        stationMap.createAllStations(stationGraph, sections);
        return stationMap;
    }

    private void createAllStations(final StationGraph stationGraph, final List<Section> sections) {
        final Set<Long> lineIds = new HashSet<>();

        for (final Section section : sections) {
            final Long lineId = section.getLineId();
            if (lineIds.contains(lineId)) {
                continue;
            }
            lineIds.add(lineId);
            stationsByLine.put(lines.getLine(lineId), stationGraph.findStations(section));
        }
    }

    public Line getLine(final Long lineId) {
        return lines.getLine(lineId);
    }

    public Set<Long> getAllLineIds() {
        return lines.getAllIds();
    }

    public Stations getStations(final Long lineId) {
        final Line line = lines.getLine(lineId);
        if (stationsByLine.containsKey(line)) {
            return stationsByLine.get(line);
        }
        throw new IllegalArgumentException("존재하지 않는 노선입니다.");
    }
}
