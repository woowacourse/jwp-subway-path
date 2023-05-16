package subway.domain.section;

import subway.domain.station.Station;
import subway.exception.InvalidDistanceException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DownDirectionStrategy implements DirectionStrategy {

    @Override
    public Sections calculate(final List<Section> sections, final Station existStation, final Station newStation, final Distance distance) {
        final Optional<Section> existSectionOptional = sections.stream()
                .filter(section -> section.getUpStation().equals(existStation))
                .findFirst();

        if (existSectionOptional.isPresent()) {
            final Section existSection = existSectionOptional.get();
            if (existSection.getDistance().isLowerOrEqualThan(distance)) {
                throw new InvalidDistanceException(distance.getDistance(), existSection.getDistanceValue());
            }
            final Section section1 = new Section(newStation, existSection.getDownStation(), existSection.getDistance().minus(distance));
            final Section section2 = new Section(existStation, newStation, distance);
            final int existIndex = sections.indexOf(existSection);
            sections.remove(existIndex);
            sections.add(existIndex, section1);
            sections.add(existIndex, section2);
        }
        if (existSectionOptional.isEmpty()) {
            sections.add(new Section(existStation, newStation, distance));
        }

        return new Sections(new LinkedList<>(sections));
    }
}
