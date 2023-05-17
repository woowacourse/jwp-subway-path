package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.SingleLineSections;
import subway.domain.Station;
import subway.repository.SectionRepository;

@Component
public class DeleteDownTerminal implements DeleteStationStrategy {

    private final SectionRepository sectionRepository;

    public DeleteDownTerminal(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(SingleLineSections sections, Station targetStation) {
        return sections.isDownTerminal(targetStation);
    }

    @Override
    public void delete(SingleLineSections sections, Station targetStation) {
        sectionRepository.delete(sections.findLastSectionId());
    }
}
