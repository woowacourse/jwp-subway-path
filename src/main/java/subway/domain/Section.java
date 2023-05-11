package subway.domain;

import java.util.Objects;

public class Section {
    private Long id;
    private Station upBoundStation;
    private Station downBoundStation;
    private Integer distance;

    public Section(Station upBoundStation, Station downBoundStation, Integer distance) {
        this.upBoundStation = upBoundStation;
        this.downBoundStation = downBoundStation;
        this.distance = distance;
    }

    public Section(Long id, Station upBoundStation, Station downBoundStation, Integer distance) {
        this.id = id;
        this.upBoundStation = upBoundStation;
        this.downBoundStation = downBoundStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Station getUpBoundStation() {
        return upBoundStation;
    }

    public Station getDownBoundStation() {
        return downBoundStation;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(id, section.id) && Objects.equals(upBoundStation, section.upBoundStation) && Objects.equals(downBoundStation, section.downBoundStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upBoundStation, downBoundStation, distance);
    }
}
