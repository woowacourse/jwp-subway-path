package subway.domain.line;

import java.util.List;
import java.util.Optional;
import subway.domain.vo.Distance;
import subway.exception.line.DistanceIsLongerThanExistingDistanceException;

public class DownAddStrategy implements AddStrategy {

    @Override
    public void activate(List<Section> sections, Station upStation, Station downStation, Distance distance) {
        // 기존 역이 upStation이다
        final Optional<Section> section = findSectionByStationExistsAtDirection(sections, upStation, Direction.UP);
        if (section.isPresent()) {
            final Section existingSection = section.get();
            if (existingSection.hasSmallerDistanceThan(distance)) {
                throw new DistanceIsLongerThanExistingDistanceException();
            }
            sections.add(new Section(downStation, existingSection.getDownStation(), existingSection.getDistance().substract(distance)));
            sections.remove(existingSection);
        }
        sections.add(new Section(upStation, downStation, distance));
    }
}
