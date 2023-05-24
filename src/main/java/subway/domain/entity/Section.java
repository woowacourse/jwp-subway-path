package subway.domain.entity;

import static subway.domain.vo.Direction.DOWN;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import subway.domain.SectionEdge;
import subway.domain.exception.IllegalSectionArgumentException;
import subway.domain.vo.Direction;
import subway.domain.vo.Distance;

public class Section {

    private final Station left;
    private final Station right;
    private final Distance distance;

    public Section(final Station left, final Station right, final Distance distance) {
        validate(left, right);
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    public static Section from(SectionEdge sectionEdge) {
        return new Section(
                sectionEdge.getSource(),
                sectionEdge.getTarget(),
                new Distance((int) sectionEdge.getWeight())
        );
    }

    public static Section createByDirection(final Station base,
                                            final Station adding,
                                            final Distance distance,
                                            final Direction direction) {
        if (direction == DOWN) {
            return new Section(base, adding, distance);
        }
        return new Section(adding, base, distance);
    }

    private void validate(final Station left, final Station right) {
        if (Objects.equals(left, right)) {
            throw new IllegalSectionArgumentException("동일한 역 간 구간을 생성할 수 없습니다.");
        }
    }


    public Optional<Section> subtract(Section other) {
        if (left.equals(other.left) && !contains(other.right)) {
            return Optional.of(new Section(other.right, right, distance.minus(other.distance)));
        }
        if (right.equals(other.right) && !contains(other.left)) {
            return Optional.of(new Section(left, other.left, distance.minus(other.distance)));
        }
        return Optional.empty();
    }

    public Optional<Section> merge(Section other) {
        Distance merged = distance.plus(other.distance);
        if (right.equals(other.left)) {
            return Optional.of(new Section(left, other.right, merged));
        }
        if (left.equals(other.right)) {
            return Optional.of(new Section(other.left, right, merged));
        }
        return Optional.empty();
    }

    private boolean contains(Station station) {
        return left.equals(station) || right.equals(station);
    }

    public Station getLeft() {
        return left;
    }

    public Station getRight() {
        return right;
    }

    public List<Station> getStations() {
        return List.of(left, right);
    }

    public Distance getDistance() {
        return distance;
    }

    public Long getLeftId() {
        return left.getId();
    }

    public Long getRightId() {
        return right.getId();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(left, section.left) && Objects.equals(right, section.right)
                && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "left=" + left +
                ", right=" + right +
                ", distance=" + distance +
                '}';
    }
}
