package subway.domain;

import subway.application.exception.SubwayServiceException;

import java.util.Iterator;
import java.util.Objects;

public class Section {

    public static final String INVALID_SECTION_MESSAGE = "동일한 역 간 구간을 생성할 수 없습니다.";
    private final Station left;
    private final Station right;
    private final Distance distance;

    public Section(final Station left, final Station right, final Distance distance) {
        validate(left, right);
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    private void validate(final Station left, final Station right) {
        if (left.equals(right)) {
            throw new SubwayServiceException(INVALID_SECTION_MESSAGE);
        }
    }

    public Station getLeft() {
        return left;
    }

    public Station getRight() {
        return right;
    }

    public int getDistance() {
        return distance.getValue();
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
