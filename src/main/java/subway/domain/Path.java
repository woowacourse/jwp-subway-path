package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import subway.domain.exception.EmptyPathException;

public class Path {

    private final List<Section> sections;

    public Path(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    // TODO: Distance로 리팩터링
    public int calculateFare() {
        int distance = getDistance();
        if (distance <= 10) {
            return 1250;
        }
        if (distance <= 50) {
            return 1250 + calculateExtraFareFor(distance - 10, 5);
        }
        return 2050 + calculateExtraFareFor(distance - 50, 8);
    }

    private int calculateExtraFareFor(int distance, int unit) {
        return ((int)(Math.ceil(distance - 1) / unit) + 1) * 100;
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> section.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public int getDistance() {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(Integer::sum)
                .orElseThrow(EmptyPathException::new);
    }
}
