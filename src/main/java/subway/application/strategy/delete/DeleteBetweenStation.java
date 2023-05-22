package subway.application.strategy.delete;

import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

@Component
public class DeleteBetweenStation extends DeleteStationStrategy {

    public DeleteBetweenStation(SectionRepository sectionRepository) {
        super(sectionRepository);
    }

    @Override
    public boolean support(SingleLineSections sections, Station targetStation) {
        return !sections.isDownTerminal(targetStation) && !sections.isUpTerminal(targetStation);
    }

    @Override
    public void delete(SingleLineSections sections, Station targetStation) {
        final Section backwardSection = sections.findIncludeSectionByForwardStation(targetStation);
        final Section forwardSection = sections.findIncludeSectionByBackwardStation(targetStation);

        final Distance newDistance = forwardSection.getDistance().plus(backwardSection.getDistance());
        final Section newSection = new Section(newDistance, forwardSection.getUpStation(), backwardSection.getDownStation(), forwardSection.getLineId());
        sectionRepository.insert(newSection);

        sectionRepository.delete(forwardSection.getId());
        sectionRepository.delete(backwardSection.getId());
    }
}
