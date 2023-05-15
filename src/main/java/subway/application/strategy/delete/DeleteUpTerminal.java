package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Sections;
import subway.domain.Station;
import subway.repository.SectionRepository;

@Component
public class DeleteUpTerminal implements DeleteStationStrategy {

    private final SectionRepository sectionRepository;

    public DeleteUpTerminal(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Sections sections, Station targetStation) {
        return sections.isUpTerminal(targetStation);
    }

    @Override
    public void delete(Sections sections, Station targetStation) {
        sectionRepository.delete(sections.findFirstSectionId());
    }
}
