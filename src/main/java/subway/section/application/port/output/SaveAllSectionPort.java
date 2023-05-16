package subway.section.application.port.output;

import subway.section.domain.Section;

import java.util.Set;

public interface SaveAllSectionPort {
    void saveAll(Set<Section> sections, Long lineId);
}
