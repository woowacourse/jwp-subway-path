package subway.application.strategy.sectioninsertion;

import subway.domain.Section;

public interface SectionInsertionStrategy {
    boolean support(Section section);

    long insert(Section section);
}
