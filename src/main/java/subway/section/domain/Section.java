package subway.section.domain;

import org.jgrapht.graph.WeightedMultigraph;
import subway.station.domain.Station;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Section {
    private final Station left;
    private final Station right;
    private final Distance distance;
    private final String lineName;
    
    public Section(final String left, final String right, final long distance, final String lineName) {
        this(new Station(left), new Station(right), new Distance(distance), lineName);
    }
    
    private Section(final Station left, final Station right, final Distance distance, final String lineName) {
        this.left = left;
        this.right = right;
        this.distance = distance;
        this.lineName = lineName;
    }
    
    public boolean hasStation(final String station) {
        return Set.of(left, right).contains(new Station(station));
    }
    
    public Set<Section> getAdditionalSectionsWithOneself(
            final String base,
            final Direction direction,
            final String additional,
            final long distance
    ) {
        final Set<Section> additionalSectionsWithOneself = new HashSet<>(Set.of(this));
        additionalSectionsWithOneself.addAll(getAdditionalSections(base, direction, additional, distance));
        return additionalSectionsWithOneself;
    }
    
    private Set<Section> getAdditionalSections(
            final String base,
            final Direction direction,
            final String additional,
            final long distance
    ) {
        if (direction.isLeft() && left.equals(new Station(base))) {
            return createLeftSection(additional, distance);
        }
        
        if (direction.isRight() && right.equals(new Station(base))) {
            return createRightSection(additional, distance);
        }
        
        return createDividedSections(additional, distance);
    }
    
    private Set<Section> createLeftSection(final String leftAdditional, final long additionalDistance) {
        final Section additionalRightSection = new Section(leftAdditional, left.getName(), additionalDistance, lineName);
        return Set.of(additionalRightSection);
    }
    
    private Set<Section> createRightSection(final String rightAdditional, final long additionalDistance) {
        final Section additionalLeftSection = new Section(right.getName(), rightAdditional, additionalDistance, lineName);
        return Set.of(additionalLeftSection);
    }
    
    private Set<Section> createDividedSections(final String betweenAdditional, final long additionalDistance) {
        validateLength(additionalDistance);
        
        final Section additionalLeftSection = new Section(left.getName(), betweenAdditional, additionalDistance, lineName);
        final Section additionalRightSection =
                new Section(betweenAdditional, right.getName(), this.distance.subtract(additionalDistance), lineName);
        return Set.of(additionalLeftSection, additionalRightSection);
    }
    
    private void validateLength(final long additionalDistance) {
        if (this.distance.lessThanOrEqualTo(additionalDistance)) {
            throw new IllegalArgumentException("분리될 Section은 추가될 Section의 거리보다 길어야합니다.");
        }
    }
    
    public Section combine(final Section otherSection) {
        final Distance combineDistance = new Distance(this.distance.add(otherSection.distance));
        if (this.right.equals(otherSection.left)) {
            return new Section(this.left, otherSection.right, combineDistance, lineName);
        }
        
        return new Section(otherSection.left, this.right, combineDistance, lineName);
    }
    
    public void putStationIfNotExist(final Set<Station> stations) {
        Set.of(this.left, this.right).forEach(station -> putStationIfNotExist(stations, station));
    }
    
    private void putStationIfNotExist(final Set<Station> stations, final Station station) {
        if (stations.contains(station)) {
            stations.remove(station);
            return;
        }
        stations.add(station);
    }
    
    public boolean isLeftStation(final Station station) {
        return this.left.equals(station);
    }
    
    public boolean isRightStation(final Station station) {
        return this.right.equals(station);
    }
    
    public void addStationsAndDistanceToGraph(final WeightedMultigraph<Station, Section> graph) {
        graph.addVertex(left);
        graph.addVertex(right);
        graph.addEdge(left, right, this);
        graph.setEdgeWeight(left, right, distance.getDistance());
    }
    
    public Station getLeft() {
        return left;
    }
    
    public Station getRight() {
        return right;
    }
    
    public Distance getDistance() {
        return distance;
    }
    
    public String getLineName() {
        return lineName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return Objects.equals(left, section.left) && Objects.equals(right, section.right) && Objects.equals(distance, section.distance) && Objects.equals(lineName, section.lineName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(left, right, distance, lineName);
    }
    
    @Override
    public String toString() {
        return "Section{" +
                "left=" + left +
                ", right=" + right +
                ", distance=" + distance +
                ", lineName='" + lineName + '\'' +
                '}';
    }
}
