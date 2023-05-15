package subway.domain.section;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subway.domain.line.Line;

public class SectionsByLine {

    private final Map<Line, Sections> sectionsByLine;

    public SectionsByLine(final Map<Line, Sections> sectionsByLine) {
        this.sectionsByLine = new HashMap<>(sectionsByLine);
    }

    public Map<Line, List<String>> getAllSortedStationNamesByLine() {
        return sectionsByLine.keySet().stream()
                .collect(toMap(
                        line -> line,
                        line -> {
                            Sections sections = sectionsByLine.get(line);
                            return sections.getSortedStationNames();
                        })
                );
    }
}
