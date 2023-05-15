package subway.application.strategy.sectioninsertion;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.EmptyDistance;
import subway.domain.EmptyStation;
import subway.domain.Section;

@Component
@Order(0)
public class InitializingSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public InitializingSectionInsertionStrategy(SectionDao sectionDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    @Override
    public boolean support(Section section) {
        return sectionDao.countStations(section.getLine()) == 0;
    }

    @Override
    public long insert(Section section) {
        final var sectionId = sectionDao.insert(section).getId();
        sectionDao.insert(new Section(section.getLine(), section.getNextStation(), new EmptyStation(), new EmptyDistance()));
        lineDao.updateHeadStation(section.getLine(), section.getPreviousStation());
        return sectionId;
    }
}
