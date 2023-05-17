package subway.application.strategy.insert;

import org.springframework.stereotype.Component;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

@Component
public class InsertTerminal implements InsertStationStrategy {

    private final SectionRepository sectionRepository;

    public InsertTerminal(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(SingleLineSections sections, InsertSection insertSection) {
        return sections.isUpTerminal(insertSection.getDownStation())
                || sections.isDownTerminal(insertSection.getUpStation());
    }

    @Override
    public Long insert(SingleLineSections sections, InsertSection insertSection) {
        final Section section = new Section(
                insertSection.getDistance(),
                insertSection.getUpStation(),
                insertSection.getDownStation(),
                insertSection.getLineId()
        );
        return sectionRepository.insert(section);
    }
}
