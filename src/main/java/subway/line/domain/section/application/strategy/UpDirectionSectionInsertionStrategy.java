package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.domain.section.infrastructure.SectionDao;
import subway.line.domain.section.Section;

@Component
@Order(1)
public class UpDirectionSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionDao sectionDao;

    public UpDirectionSectionInsertionStrategy(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public boolean support(Section section) {
        return sectionDao.findByNextStation(section.getNextStation(), section.getLine()).isPresent();
    }

    @Override
    public long insert(Section section) {
        final var savedSection = sectionDao.insert(section);

        final var stationToUpdate = sectionDao.findByNextStation(section.getNextStation(), section.getLine())
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));
        sectionDao.update(stationToUpdate.change()
                .line(section.getLine())
                .nextStation(section.getPreviousStation())
                .subtractDistance(section.getDistance())
                .done());

        return savedSection.getId();
    }
}
