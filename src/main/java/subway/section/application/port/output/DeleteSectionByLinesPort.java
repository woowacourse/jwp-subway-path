package subway.section.application.port.output;

import subway.line.domain.Line;

import java.util.Set;

public interface DeleteSectionByLinesPort {
    
    void deleteSectionByLines(Set<Line> modifiedLines);
}
