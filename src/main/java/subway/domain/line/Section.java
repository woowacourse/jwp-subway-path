package subway.domain.line;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station left;
    private final Station right;
    private final Distance distance;

    public Section(Long id, Station left, Station right, Distance distance) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.distance = distance;
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(getId(), section.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
