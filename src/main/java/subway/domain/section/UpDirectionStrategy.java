package subway.domain.section;

import subway.domain.station.Station;
import subway.exception.InvalidDistanceException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UpDirectionStrategy implements DirectionStrategy {

    @Override
    public Sections calculate(final List<Section> sections, final Station existStation, final Station newStation, final Distance distance) {
        final Optional<Section> existSectionOptional = sections.stream()
                .filter(section -> section.getDownStation().equals(existStation))
                .findFirst();

        if (existSectionOptional.isPresent()) {
            final Section existSection = existSectionOptional.get();
            if (existSection.getDistance().isLowerOrEqualThan(distance)) {
                throw new InvalidDistanceException(distance.getDistance(), existSection.getDistanceValue());
            }
            final Section section1 = new Section(existSection.getUpStation(), newStation, existSection.getDistance().minus(distance));
            final Section section2 = new Section(newStation, existStation, distance);
            final int existIndex = sections.indexOf(existSection);
            sections.remove(existIndex);
            sections.add(existIndex, section2);
            sections.add(existIndex, section1);
        }
        if (existSectionOptional.isEmpty()) {
            sections.add(0, new Section(newStation, existStation, distance));
        }

        return new Sections(new LinkedList<>(sections));
    }
}
