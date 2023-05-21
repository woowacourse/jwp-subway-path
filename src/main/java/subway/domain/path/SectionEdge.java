package subway.domain.path;

import subway.domain.core.Section;

public interface SectionEdge {

    Section toSection();

    int getSurcharge();

    long getLineId();
}
