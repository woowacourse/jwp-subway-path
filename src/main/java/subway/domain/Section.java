package subway.domain;

import subway.exception.InvalidDistanceException;
import subway.exception.SectionMergeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Section {

    public static final int MINIMUM_DISTANCE = 1;

    private final Station upstream;
    private final Station downstream;
    private final int distance;

    public Section(Station upstream, Station downstream, int distance) {
        validateDistance(distance);
        this.upstream = upstream;
        this.downstream = downstream;
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new InvalidDistanceException("거리는 " + MINIMUM_DISTANCE + "이상이어야 합니다");
        }
    }

    public boolean isCorrespondingSection(Station upstream, Station downstream) {
        return this.upstream.equals(upstream) && this.downstream.equals(downstream);
    }

    public List<Section> insertInTheMiddle(Station newStation, int distanceToUpstream) {
        List<Section> split = new ArrayList<>();
        int distanceToDownstream = distance - distanceToUpstream;

        if (upstream.equals(Station.getEmptyEndpoint())) {
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
        Optional<Station> optionalStation = getLinkingStation(sectionToMerge);
        Station linkingStation = optionalStation.orElseThrow(() -> new SectionMergeException("연결할 수 없는 구간입니다."));
        int mergedSectionDistance = distance + sectionToMerge.distance;
        if (downstream.equals(linkingStation)) {
            return new Section(upstream, sectionToMerge.downstream, mergedSectionDistance);
        }
        return new Section(downstream, sectionToMerge.upstream, mergedSectionDistance);
    }

    private Optional<Station> getLinkingStation(Section otherSection) {
        if (downstream.equals(otherSection.upstream)) {
            return Optional.of(downstream);
        }
        if (upstream.equals(otherSection.downstream)) {
            return Optional.of(upstream);
        }
        return Optional.empty();
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
        return distance == section.distance && Objects.equals(upstream, section.upstream) && Objects.equals(downstream, section.downstream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upstream, downstream, distance);
    }
}
