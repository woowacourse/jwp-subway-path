package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Station;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

@Component
public class DeleteDownTerminal extends DeleteStationStrategy {

    private final SectionRepository sectionRepository;

    public DeleteDownTerminal(SectionRepository sectionRepository, SectionRepository sectionRepository1) {
        super(sectionRepository);
        this.sectionRepository = sectionRepository1;
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
