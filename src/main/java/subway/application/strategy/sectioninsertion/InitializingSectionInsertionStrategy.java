package subway.application.strategy.sectioninsertion;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Distance;
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
        return sectionDao.isLineEmpty(section.getLine());
    }

    @Override
    public long insert(Section section) {
        final var sectionId = sectionDao.insert(section).getId();
        sectionDao.insert(new Section(section.getLine(), section.getNextStation(), new EmptyStation(), Distance.emptyDistance()));
        lineDao.updateHeadStation(section.getLine(), section.getPreviousStation());
        return sectionId;
    }
}
