package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Objects;

public class Section extends DefaultWeightedEdge {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(final Long id, final Station upStation, final Station downStation, final int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(final Station upStation, final Station downStation, final int distance) {
        this(null, upStation, downStation, distance);
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

    @Override
    protected Station getSource() {
        return upStation;
    }

    @Override
    protected Station getTarget() {
        return downStation;
    }

    @Override
    protected double getWeight() {
        return distance;
    }

    @Override
    public String toString() {
        return "Section{" +
                "upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return distance == section.distance && Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }
}
