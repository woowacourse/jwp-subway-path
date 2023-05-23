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
import subway.domain.section.Sections;

public class SubwayMap {

    private final Lines lines;
    private final Map<Line, Sections> sectionsByLine;

    private SubwayMap(final Lines lines, final Map<Line, Sections> sectionsByLine) {
        this.lines = lines;
        this.sectionsByLine = sectionsByLine;
    }

    public static SubwayMap of(final List<Line> lines, final List<Section> sections) {
        final SubwayMap subwayMap = new SubwayMap(Lines.of(lines), new HashMap<>());
        final StationGraph stationGraph = StationGraph.of(sections);

        subwayMap.createAllSections(stationGraph, sections);
        return subwayMap;
    }

    private void createAllSections(final StationGraph stationGraph, final List<Section> sections) {
        final Set<Long> lineIds = new HashSet<>();

        for (final Section section : sections) {
            final Long lineId = section.getLineId();
            if (lineIds.contains(lineId)) {
                continue;
            }
            lineIds.add(lineId);
            sectionsByLine.put(lines.getLine(lineId), stationGraph.findSections(section));
        }
    }

    public Line getLine(final Long lineId) {
        return lines.getLine(lineId);
    }

    public Set<Long> getAllLineIds() {
        return lines.getAllIds();
    }

    public Sections getSections(final Long lineId) {
        final Line line = lines.getLine(lineId);
        if (sectionsByLine.containsKey(line)) {
            return sectionsByLine.get(line);
        }
        throw new IllegalArgumentException("존재하지 않는 노선입니다.");
    }
}
