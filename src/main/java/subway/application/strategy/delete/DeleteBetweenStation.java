package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

@Component
public class DeleteBetweenStation implements DeleteStationStrategy {

    private final SectionRepository sectionRepository;

    public DeleteBetweenStation(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(SingleLineSections sections, Station targetStation) {
        return sections.isBetweenStation(targetStation);
    }

    @Override
    public void delete(SingleLineSections sections, Station targetStation) {
        final SingleLineSections includeTargetSection = sections.findIncludeTargetSection(targetStation);
        final Distance newDistance = includeTargetSection.calculateTotalDistance();

        final Section forwardSection = includeTargetSection.getSections().get(0);
        final Section backwardSection = includeTargetSection.getSections().get(1);

        final Section newSection = new Section(newDistance, forwardSection.getUpStation(), backwardSection.getDownStation(), forwardSection.getLineId());
        sectionRepository.insert(newSection);

        for (Section section : includeTargetSection.getSections()) {
            sectionRepository.delete(section.getId());
        }
    }
}
