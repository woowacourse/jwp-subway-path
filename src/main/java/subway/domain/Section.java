package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import subway.exception.InvalidDistanceException;

import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
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

        Section firstSection = new Section(upstream, newStation, distanceToUpstream);
        Section secondSection = new Section(newStation, downstream, distance - distanceToUpstream);
        split.add(firstSection);
        split.add(secondSection);

        return split;
    }

    public boolean contains(Station station) {
        return upstream.equals(station) || downstream.equals(station);
    }
}
