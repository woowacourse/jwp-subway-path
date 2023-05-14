package subway.line.domain;

import subway.line.exception.InvalidDistanceException;
import subway.line.exception.InvalidStationNameException;
import subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MiddleSection extends AbstractSection {

    public static final int MINIMUM_DISTANCE = 1;

    private final int distance;

    public MiddleSection(Station upstream, Station downstream, int distance) {
        super(upstream, downstream);
        validateNotDummyTerminalStation(upstream, downstream);
        validateDistance(distance);
        this.distance = distance;
    }

    public MiddleSection(MiddleSection otherSection) {
        this(otherSection.getUpstream(), otherSection.getDownstream(), otherSection.distance);
    }

    private void validateNotDummyTerminalStation(Station upstream, Station downstream) {
        final Station duummyTerminalStation = DummyTerminalStation.getInstance();

        if (upstream.equals(duummyTerminalStation) || downstream.equals(duummyTerminalStation)) {
            throw new InvalidStationNameException("역 이름은 공백이 될 수 없습니다");
        }
    }

    private void validateDistance(int distance) {
        if (distance < MINIMUM_DISTANCE) {
            throw new InvalidDistanceException("상,하행역 간의 거리는 " + MINIMUM_DISTANCE + "이상이어야 합니다");
        }
    }

    @Override
    public List<AbstractSection> insertInTheMiddle(Station stationToAdd, int distanceToUpstream) {
        AbstractSection firstSection = new MiddleSection(getUpstream(), stationToAdd, distanceToUpstream);
        AbstractSection secondSection = new MiddleSection(stationToAdd, getDownstream(), distance - distanceToUpstream);

        return new ArrayList<>(List.of(firstSection, secondSection));
    }

    @Override
    public MiddleSection merge(AbstractSection sectionToMerge) {
        validateIsMiddleSection(sectionToMerge);
        validateSectionLinked(sectionToMerge);

        return mergeValidatedSection((MiddleSection) sectionToMerge);
    }

    private void validateIsMiddleSection(AbstractSection sectionToMerge) {
        if (sectionToMerge.getClass() != this.getClass()) {
            throw new IllegalArgumentException("디버깅: MiddleSection에 MiddleSection이 아닌 Section을 인자로 merge()하려 했습니다.");
        }
    }

    private MiddleSection mergeValidatedSection(MiddleSection sectionToMerge) {
        if (getDownstream().equals(sectionToMerge.getUpstream())) {
            return new MiddleSection(getUpstream(), sectionToMerge.getDownstream(), distance + sectionToMerge.distance);
        }
        return sectionToMerge.mergeValidatedSection(this);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MiddleSection that = (MiddleSection) o;
        return distance == that.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), distance);
    }

    @Override
    public String toString() {
        return "MiddleSection{" +
                super.toString() +
                "distance=" + distance +
                '}';
    }
}
