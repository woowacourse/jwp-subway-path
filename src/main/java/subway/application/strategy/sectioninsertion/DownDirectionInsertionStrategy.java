package subway.application.strategy.sectioninsertion;

import subway.dao.SectionDao;
import subway.domain.Section;

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
                .orElseThrow(IllegalStateException::new);

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
