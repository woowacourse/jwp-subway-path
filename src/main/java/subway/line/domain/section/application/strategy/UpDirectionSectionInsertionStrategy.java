package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionRepository;

@Component
@Order(1)
public class UpDirectionSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionRepository sectionRepository;

    public UpDirectionSectionInsertionStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Section section) {
        return sectionRepository.findByNextStation(section.getNextStation(), section.getLine()).isPresent();
    }

    @Override
    public long insert(Section section) {
        final var savedSection = sectionRepository.insert(section);

        final var stationToUpdate = sectionRepository.findByNextStation(section.getNextStation(), section.getLine())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));
        sectionRepository.update(stationToUpdate.change()
                .line(section.getLine())
                .nextStation(section.getPreviousStation())
                .subtractDistance(section.getDistance())
                .done());

        return savedSection.getId();
    }
}
