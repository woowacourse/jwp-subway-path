package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.application.LineRepository;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionRepository;

@Component
@Order(4)
public class HighestSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public HighestSectionInsertionStrategy(SectionRepository sectionRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean support(Section section) {
        return section.getLine().getHead().equals(section.getNextStation());
    }

    @Override
    public long insert(Section section) {
        final var sectionId = sectionRepository.insert(section).getId();
        lineRepository.updateHeadStation(section.getLine(), section.getPreviousStation());
        return sectionId;
    }
}
