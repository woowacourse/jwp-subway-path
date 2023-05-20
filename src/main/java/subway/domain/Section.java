package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final int distance;
    private int order;

    public Section(final Long id, final Station upStation, final Station downStation, final int distance, final int order) {
        validateDistance(distance);
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.order = order;
    }

    public Section(final Station upStation, final Station downStation, final int distance, final int order) {
        this(null, upStation, downStation, distance, order);
    }

    public Section(final Station upStation, final Station downStation, final int distance) {
        this(null, upStation, downStation, distance, 0);
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0 이상이어야 합니다.");
        }
    }

    public boolean containStation(final Station station) {
        return station.equals(upStation) || station.equals(downStation);
    }

    public boolean isSameSection(final Section section) {
        return upStation.equals(section.getUpStation())
                && downStation.equals(section.getDownStation());
    }

    public void initOrder() {
       order = 1;
    }

    public void updateOrder(final int newOrder) {
        order = newOrder;
    }

    public void increaseOrder() {
        order = order + 1;
    }

    public void decreaseOrder() {
        order = order - 1;
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && order == section.order && Objects.equals(id, section.id) && upStation.equals(section.upStation) && downStation.equals(section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upStation, downStation, distance, order);
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                ", order=" + order +
                '}';
    }
}
