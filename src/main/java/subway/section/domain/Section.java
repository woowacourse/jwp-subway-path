package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.station.domain.Station;

@Getter
@ToString
@EqualsAndHashCode
public class Section {
    private final Station leftStation;
    private final Station rightStation;
    private final Distance distance;
    
    public Section(final String leftStation, final String rightStation, final long distance) {
        this(new Station(leftStation), new Station(rightStation), new Distance(distance));
    }
    
    public Section(final Station leftStation, final Station rightStation, final Distance distance) {
        this.leftStation = leftStation;
        this.rightStation = rightStation;
        this.distance = distance;
    }
    
    public Section createLeftSection(final String additionalStation, final long additionalDistance) {
        return new Section(additionalStation, leftStation.getName(), additionalDistance);
    }
    
    public Section createRightSection(final String additionalStation, final long additionalDistance) {
        return new Section(rightStation.getName(), additionalStation, additionalDistance);
    }
}
