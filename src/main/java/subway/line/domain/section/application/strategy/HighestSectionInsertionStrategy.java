package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.infrastructure.LineDao;
import subway.line.domain.section.infrastructure.SectionDao;
import subway.line.domain.section.Section;

@Component
@Order(4)
public class HighestSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public HighestSectionInsertionStrategy(SectionDao sectionDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    @Override
    public boolean support(Section section) {
        return section.getLine().getHead().equals(section.getNextStation());
    }

    @Override
    public long insert(Section section) {
        final var sectionId = sectionDao.insert(section).getId();
        lineDao.updateHeadStation(section.getLine(), section.getPreviousStation());
        return sectionId;
    }
}
