package subway.domain.subwaymap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import subway.domain.graph.StationGraph;
import subway.domain.line.Line;
import subway.domain.line.Lines;
import subway.domain.section.Section;
import subway.domain.station.Stations;

public class SubwayStationMap {

    private final Lines lines;
    private final Map<Line, Stations> stationsByLine;

    private SubwayStationMap(final Lines lines, final Map<Line, Stations> stationsByLine) {
        this.lines = lines;
        this.stationsByLine = stationsByLine;
    }

    public static SubwayStationMap of(final List<Line> lines, final List<Section> sections) {
        final SubwayStationMap subwayStationMap = new SubwayStationMap(Lines.of(lines), new HashMap<>());
        final StationGraph stationGraph = StationGraph.of(sections);

        subwayStationMap.createAllStations(stationGraph, sections);
        return subwayStationMap;
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
