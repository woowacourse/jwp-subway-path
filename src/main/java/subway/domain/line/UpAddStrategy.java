package subway.domain.line;

import java.util.List;
import java.util.Optional;
import subway.domain.Distance;

public class UpAddStrategy implements AddStrategy {
    @Override
    public void activate(List<Section> sections, Station upStation, Station downStation, Distance distance) {
        // 기존 역이 downStation이다.
        final Optional<Section> section = findSectionByStationExistsAtDirection(sections, downStation, Direction.DOWN);
        if (section.isPresent()) {
            final Section existingSection = section.get();
            if (existingSection.hasSmallerDistanceThan(distance)) {
                throw new IllegalArgumentException("추가하려는 거리가 기존의 거리보다 깁니다.");
            }
            sections.add(new Section(existingSection.getUpStation(), upStation, existingSection.getDistance().substract(distance)));
            sections.remove(existingSection);
        }
        sections.add(new Section(upStation, downStation, distance));
    }
}
