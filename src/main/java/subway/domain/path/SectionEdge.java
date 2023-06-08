package subway.domain.path;

import subway.domain.section.Section;

public interface SectionEdge {

    Section getSection();

    Long getLineId();
}
