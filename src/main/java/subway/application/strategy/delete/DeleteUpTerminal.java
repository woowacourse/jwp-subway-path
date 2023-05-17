package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Station;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

@Component
public class DeleteUpTerminal implements DeleteStationStrategy {

    private final SectionRepository sectionRepository;

    public DeleteUpTerminal(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(SingleLineSections sections, Station targetStation) {
        return sections.isUpTerminal(targetStation);
    }

    @Override
    public void delete(SingleLineSections sections, Station targetStation) {
        sectionRepository.delete(sections.findFirstSectionId());
    }
}
