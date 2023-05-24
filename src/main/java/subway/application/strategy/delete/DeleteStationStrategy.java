package subway.application.strategy.delete;

import subway.domain.Station;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

abstract class DeleteStationStrategy {

    protected final SectionRepository sectionRepository;

    public DeleteStationStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    protected abstract boolean support(SingleLineSections sections, Station targetStation);

    protected abstract void delete(SingleLineSections sections, Station targetStation);
}
