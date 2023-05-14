package subway.application.strategy.sectioninsertion;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.application.exception.ExceptionMessages;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.EmptyStation;
import subway.domain.Section;

@Component
@Order(2)
public class LowestSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionDao sectionDao;

    public LowestSectionInsertionStrategy(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public boolean support(Section section) {
        final var previousSection = sectionDao.findByPreviousStation(section.getPreviousStation(), section.getLine());
        return previousSection.isPresent() && previousSection.get().isNextStationEmpty();
    }

    @Override
    public long insert(Section section) {
        final var sectionToUpdate = sectionDao.findByPreviousStation(section.getPreviousStation(), section.getLine())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var savedId = sectionDao.insert(section.change()
                .previousStation(section.getNextStation())
                .nextStation(new EmptyStation())
                .distance(Distance.emptyDistance())
                .done()).getId();

        sectionDao.update(sectionToUpdate.change()
                .previousStation(section.getPreviousStation())
                .nextStation(section.getNextStation())
                .distance(section.getDistance())
                .done());

        return savedId;
    }
}
