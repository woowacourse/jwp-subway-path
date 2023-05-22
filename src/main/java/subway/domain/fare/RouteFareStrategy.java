package subway.domain.fare;

import java.util.List;
import subway.domain.section.PathSection;
import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;

class RouteFareStrategy implements FareStrategy {

    @Override
    public double calculateFare(final double fare, final Passenger passenger, final Subway subway) {
        final List<PathSection> sections = subway.findShortestPathSections(passenger.getStart(), passenger.getEnd());
        final int highestFareOfLine = findHighestFareOfLine(sections);
        return fare + highestFareOfLine;
    }

    private int findHighestFareOfLine(final List<PathSection> sections) {
        return sections.stream()
                .map(PathSection::getFareOfLine)
                .reduce(0, Integer::max);
    }
}
