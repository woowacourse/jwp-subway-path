package subway.domain;

import static subway.domain.Direction.LEFT;
import static subway.domain.Direction.RIGHT;

import java.util.ArrayList;
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
        this.sections = new ArrayList<>(sections);
    }

    public boolean containsAll(final Station start, final Station end) {
        return sections.stream()
                .anyMatch(section -> section.containsAll(start, end));
    }

    public void add(final Station base, final Station additional, final Distance distance, final Direction direction) {
        validate(base, additional, distance, direction);

        if (direction == RIGHT) {
            final Optional<Section> sectionAtStart = findSectionAtStart(base);
            if (sectionAtStart.isPresent()) {
                final Section originSection = sectionAtStart.get();
                changeDistance(additional, originSection.getEnd(), originSection, distance);
            }
            sections.add(new Section(base, additional, distance));
        }

        if (direction == LEFT) {
            final Optional<Section> sectionAtEnd = findSectionAtEnd(base);
            if (sectionAtEnd.isPresent()) {
                final Section originSection = sectionAtEnd.get();
                changeDistance(originSection.getStart(), additional, originSection, distance);
            }
            sections.add(new Section(additional, base, distance));
        }
    }

    private void validate(
            final Station base,
            final Station additional,
            final Distance distance,
            final Direction direction
    ) {
        if (!contains(base)) {
            throw new InvalidSectionException("기준역이 존재하지 않습니다.");
        }
        if (contains(additional)) {
            throw new InvalidSectionException("등록할 역이 이미 존재합니다.");
        }
        if (isNotValidDistanceToAddRight(base, distance, direction)
                || isNotValidDistanceToAddLeft(base, distance, direction)) {
            throw new InvalidSectionException("등록할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
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
        final Optional<Section> findSection = findSectionAtStart(base);
        return findSection.map(section -> section.moreThanOrEqual(distance))
                .orElse(false);
    }

    private Optional<Section> findSectionAtStart(final Station base) {
        return sections.stream()
                .filter(section -> section.isStart(base))
                .findFirst();
    }

    private boolean isNotValidDistanceToAddLeft(
            final Station base,
            final Distance distance,
            final Direction direction
    ) {
        if (direction == RIGHT) {
            return false;
        }
        final Optional<Section> findSection = findSectionAtEnd(base);
        return findSection.map(section -> section.moreThanOrEqual(distance))
                .orElse(false);
    }

    private Optional<Section> findSectionAtEnd(final Station base) {
        return sections.stream()
                .filter(section -> section.isEnd(base))
                .findFirst();
    }

    private void changeDistance(
            final Station start,
            final Station end,
            final Section originSection,
            final Distance distance
    ) {
        sections.add(new Section(start, end, originSection.subtract(distance)));
        sections.remove(originSection);
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                '}';
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
