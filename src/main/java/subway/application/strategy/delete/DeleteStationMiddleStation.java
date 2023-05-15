package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.repository.SectionRepository;

@Component
public class DeleteStationMiddleStation extends DeleteStationStrategy {

    public DeleteStationMiddleStation(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    boolean support(Sections sections, Station targetStation) {
        return !sections.isDownEndPoint(targetStation) && !sections.isUpEndPoint(targetStation);
    }

    @Override
    void delete(Sections sections, Station targetStation) {
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
