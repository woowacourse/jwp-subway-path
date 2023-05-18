package subway.line.domain.section.application.strategy;

import subway.line.domain.section.Section;

public interface SectionInsertionStrategy {
    boolean support(Section section);

    long insert(Section section);
}
