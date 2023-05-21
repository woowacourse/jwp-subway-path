package subway.domain.strategy;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;
import java.util.Optional;

public class UpStrategy implements DirectionStrategy {
    @Override
    public Section createSectionWith(Station baseStation, Station newStation, Distance distance, long lineId) {
        return new Section(baseStation, newStation, distance, lineId);
    }

    @Override
    public Optional<Section> findSection(Station baseStation, List<Section> sections) {
        return sections.stream()
                .filter(section -> section.hasDownStation(baseStation))
                .findFirst();
    }

    @Override
    public Section createSectionBasedOn(Section originalSection, Section newSection) {
        Distance newDistance = originalSection.subtract(newSection);
        return new Section(
                originalSection.getUpStation(),
                newSection.getUpStation(),
                newDistance,
                originalSection.getLineId());
    }

}
