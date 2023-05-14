package subway.application.strategy.sectioninsertion;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.application.exception.ExceptionMessages;
import subway.dao.SectionDao;
import subway.domain.Section;

@Component
@Order(3)
public class DownDirectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionDao sectionDao;

    public DownDirectionInsertionStrategy(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public boolean support(Section section) {
        final var previousSection = sectionDao.findByPreviousStation(section.getPreviousStation(), section.getLine());
        return previousSection.isPresent() && !previousSection.get().isNextStationEmpty();
    }

    @Override
    public long insert(Section section) {
        final var sectionToUpdate = sectionDao.findByPreviousStation(section.getPreviousStation(), section.getLine())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var sectionId = sectionDao.insert(section.change()
                .previousStation(section.getNextStation())
                .nextStation(sectionToUpdate.getNextStation())
                .distance(sectionToUpdate.getDistance().subtract(section.getDistance()))
                .done()).getId();

        sectionDao.update(sectionToUpdate.change()
                .nextStation(section.getNextStation())
                .distance(section.getDistance())
                .done());

        return sectionId;
    }
}
