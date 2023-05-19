package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;

@Component
@Order(2)
public class LowestSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionRepository sectionRepository;

    public LowestSectionInsertionStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Section section) {
        final var previousSection = sectionRepository.findByPreviousStation(section.getPreviousStation(), section.getLine());
        return previousSection.isPresent() && previousSection.get().isNextStationEmpty();
    }

    @Override
    public long insert(Section section) {
        final var sectionToUpdate = sectionRepository.findByPreviousStation(section.getPreviousStation(), section.getLine())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var savedId = sectionRepository.insert(section.change()
                .previousStation(section.getNextStation())
                .nextStation(new EmptyStation())
                .distance(new EmptyDistance())
                .done()).getId();

        sectionRepository.update(sectionToUpdate.change()
                .previousStation(section.getPreviousStation())
                .nextStation(section.getNextStation())
                .distance(section.getDistance())
                .done());

        return savedId;
    }
}
