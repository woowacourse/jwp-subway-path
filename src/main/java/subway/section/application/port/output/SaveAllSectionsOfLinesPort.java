package subway.section.application.port.output;

import subway.line.domain.Line;

import java.util.Set;

public interface SaveAllSectionsOfLinesPort {
    void saveAllSectionsOfLines(Set<Line> modifiedLines);
}
