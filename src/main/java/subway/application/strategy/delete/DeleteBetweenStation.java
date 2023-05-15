package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.repository.SectionRepository;

@Component
public class DeleteBetweenStation implements DeleteStationStrategy {

    private final SectionRepository sectionRepository;

    public DeleteBetweenStation(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Sections sections, Station targetStation) {
        return sections.isBetweenStation(targetStation);
    }

    @Override
    public void delete(Sections sections, Station targetStation) {
        final Sections includeTargetSection = sections.findIncludeTargetSection(targetStation);
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
