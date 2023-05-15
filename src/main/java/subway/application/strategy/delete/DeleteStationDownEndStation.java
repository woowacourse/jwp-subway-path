package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Sections;
import subway.domain.Station;
import subway.repository.SectionRepository;

@Component
public class DeleteStationDownEndStation extends DeleteStationStrategy {

    public DeleteStationDownEndStation(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    boolean support(Sections sections, Station targetStation) {
        return sections.isDownEndPoint(targetStation);
    }

    @Override
    void delete(Sections sections, Station targetStation) {
        sectionRepository.delete(sections.findLastSectionId());
    }
}
