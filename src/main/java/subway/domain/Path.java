package subway.domain;

import java.util.ArrayList;
import java.util.List;

import subway.domain.exception.EmptyPathException;

public class Path {

    private final List<Section> sections;

    public Path(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    // TODO: ENUM 리팩터링
    public int calculateFare() {
        int distance = getDistanceOf(sections);
        if (distance <= 10) {
            return 1250;
        }
        if (distance < 50) {
            return 1250 + (distance - 10) / 5 * 100;
        }
        return 2050 + (distance - 50) / 8 * 100;
    }

    private int getDistanceOf(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(Integer::sum)
                .orElseThrow(EmptyPathException::new);
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections);
    }
}
