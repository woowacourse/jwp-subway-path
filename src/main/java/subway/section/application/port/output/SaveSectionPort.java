package subway.section.application.port.output;

import subway.section.domain.Section;

public interface SaveSectionPort {
    Long save(Section section, Long lineId);
}
