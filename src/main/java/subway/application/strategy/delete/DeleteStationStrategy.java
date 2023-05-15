package subway.application.strategy.delete;

import subway.domain.Sections;
import subway.domain.Station;
import subway.repository.SectionRepository;

public abstract class DeleteStationStrategy {

    protected final SectionRepository sectionRepository;

    public DeleteStationStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    abstract boolean support(Sections sections, Station targetStation);

    abstract void delete(Sections sections, Station targetStation);
}
