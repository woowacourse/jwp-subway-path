package subway.application.strategy.sectioninsertion;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Section;

public class HighestSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public HighestSectionInsertionStrategy(SectionDao sectionDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    @Override
    public boolean support(Section section) {
        return lineDao.findHeadStation(section.getLine()).equals(section.getNextStation());
    }

    @Override
    public long insert(Section section) {
        final var sectionId = sectionDao.insert(section).getId();
        lineDao.updateHeadStation(section.getLine(), section.getPreviousStation());
        return sectionId;
    }
}
