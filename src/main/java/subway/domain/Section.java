package subway.domain;

import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.InvalidDistanceException;
import subway.exception.SectionMergeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static subway.domain.Line.EMPTY_ENDPOINT_STATION;

public class Section {

    private static final int MINIMUM_DISTANCE = 1;

    private Long id;
    private final Station upstream;
    private final Station downstream;
    private final int distance;

    public Section(Station upstream, Station downstream, int distance) {
        validateDistance(distance);
        this.upstream = upstream;
        this.downstream = downstream;
        this.distance = distance;
    }

    public Section(long id, Station upstream, Station downstream, int distance) {
        this(upstream, downstream, distance);
        this.id = id;
    }

    private void validateDistance(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new InvalidDistanceException("거리는 " + MINIMUM_DISTANCE + "이상이어야 합니다");
        }
    }

    public boolean containsSameStations(Station upstream, Station downstream) {
        return this.upstream.equals(upstream) && this.downstream.equals(downstream);
    }

    public List<Section> insertInTheMiddle(Station newStation, int distanceToUpstream) {
        List<Section> split = new ArrayList<>();
        int distanceToDownstream = distance - distanceToUpstream;

        if (upstream.equals(EMPTY_ENDPOINT_STATION)) {
            distanceToDownstream = distanceToUpstream;
            distanceToUpstream = Integer.MAX_VALUE;
        }
        Section firstSection = new Section(upstream, newStation, distanceToUpstream);
        Section secondSection = new Section(newStation, downstream, distanceToDownstream);
        split.add(firstSection);
        split.add(secondSection);

        return split;
    }

    public boolean contains(Station station) {
        return upstream.equals(station) || downstream.equals(station);
    }

    public Section merge(Section sectionToMerge) {
        validateLinkingStation(sectionToMerge);
        int mergedSectionDistance = calculateDistance(sectionToMerge);
        if (downstream.equals(sectionToMerge.upstream)) {
            return new Section(upstream, sectionToMerge.downstream, mergedSectionDistance);
        }
        return new Section(sectionToMerge.upstream, downstream, mergedSectionDistance);
    }

    private void validateLinkingStation(Section sectionToMerge) {
        if (!downstream.equals(sectionToMerge.upstream) && !upstream.equals(sectionToMerge.downstream)) {
            throw new SectionMergeException("연결할 수 없는 구간입니다.");
        }
    }

    private int calculateDistance(Section sectionToMerge) {
        if (distance == Integer.MAX_VALUE || sectionToMerge.distance == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return distance + sectionToMerge.distance;
    }

    public WeightedMultigraph<Station, Section> addWeightedEdges(WeightedMultigraph<Station, Section> graph) {
        graph.addEdge(upstream, downstream, this);
        graph.setEdgeWeight(upstream, downstream, distance);
        return graph;
    }

    public int getDistance() {
        return distance;
    }

    public Station getUpstream() {
        return upstream;
    }

    public Station getDownstream() {
        return downstream;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Section{" +
                "upstream=" + upstream +
                ", downstream=" + downstream +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return distance == section.distance && Objects.equals(id, section.id) && Objects.equals(upstream, section.upstream) && Objects.equals(downstream, section.downstream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upstream, downstream, distance);
    }
}
