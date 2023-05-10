package subway.domain;

import static subway.domain.Direction.LEFT;
import static subway.domain.Direction.RIGHT;

import java.util.List;
import java.util.Optional;
import subway.exception.InvalidSectionException;

public class Line {

    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(final String name, final String color, final List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public boolean containsAll(final Station start, final Station end) {
        return sections.stream()
                .anyMatch(section -> section.containsAll(start, end));
    }

    public void add(final Station base, final Station additional, final Distance distance, final Direction direction) {
        if (!contains(base)) {
            throw new InvalidSectionException("기준역이 존재하지 않습니다.");
        }
        if (contains(additional)) {
            throw new InvalidSectionException("추가할 역이 이미 존재합니다.");
        }
        if (isNotValidDistanceToAddRight(base, distance, direction)
                || isNotValidDistanceToAddLeft(base, distance, direction)) {
            throw new InvalidSectionException("추가할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
        }
    }

    private boolean contains(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private boolean isNotValidDistanceToAddRight(
            final Station base,
            final Distance distance,
            final Direction direction
    ) {
        if (direction == LEFT) {
            return false;
        }
        final Optional<Section> findSection = sections.stream()
                .filter(section -> section.isStart(base))
                .findFirst();
        return findSection.map(section -> section.moreThanOrEqual(distance))
                .orElse(false);
    }

    private boolean isNotValidDistanceToAddLeft(
            final Station base,
            final Distance distance,
            final Direction direction
    ) {
        if (direction == RIGHT) {
            return false;
        }
        final Optional<Section> findSection = sections.stream()
                .filter(section -> section.isEnd(base))
                .findFirst();
        return findSection.map(section -> section.moreThanOrEqual(distance))
                .orElse(false);
    }


    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
