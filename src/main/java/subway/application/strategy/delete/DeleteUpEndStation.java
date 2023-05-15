package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Sections;
import subway.domain.Station;
import subway.repository.SectionRepository;

@Component
public class DeleteUpEndStation extends DeleteStationStrategy {

    public DeleteUpEndStation(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    boolean support(Sections sections, Station targetStation) {
        return sections.isUpEndPoint(targetStation);
    }

    @Override
    void delete(Sections sections, Station targetStation) {
        sectionRepository.delete(sections.findFirstSectionId());
    }
}
