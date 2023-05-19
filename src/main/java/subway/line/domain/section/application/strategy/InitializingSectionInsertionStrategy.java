package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.application.LineRepository;
import subway.line.domain.section.application.SectionRepository;
import subway.line.infrastructure.LineDao;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.section.Section;

@Component
@Order(0)
public class InitializingSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public InitializingSectionInsertionStrategy(SectionRepository sectionRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean support(Section section) {
        return sectionRepository.countStations(section.getLine()) == 0;
    }

    @Override
    public long insert(Section section) {
        final var sectionId = sectionRepository.insert(section).getId();
        sectionRepository.insert(new Section(section.getLine(), section.getNextStation(), new EmptyStation(), new EmptyDistance()));
        lineRepository.updateHeadStation(section.getLine(), section.getPreviousStation());
        return sectionId;
    }
}
