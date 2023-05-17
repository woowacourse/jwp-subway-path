package subway.application.port.out.section;

import subway.domain.Section;

import java.util.List;

public interface SectionCommandPort {
    void saveSection(Long lineId, List<Section> sections);
}
