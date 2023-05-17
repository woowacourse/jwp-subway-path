package subway.domain.section.general;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subway.domain.line.Line;

public class GeneralSectionsByLine {

    private final Map<Line, GeneralSections> generalSectionsByLine;

    public GeneralSectionsByLine(final Map<Line, GeneralSections> generalSectionsByLine) {
        this.generalSectionsByLine = new HashMap<>(generalSectionsByLine);
    }

    public Map<Line, List<String>> getAllSortedStationNamesByLine() {
        return generalSectionsByLine.keySet().stream()
                .collect(toMap(
                        line -> line,
                        line -> {
                            GeneralSections generalSections = generalSectionsByLine.get(line);
                            return generalSections.getSortedStationNames();
                        })
                );
    }
}
