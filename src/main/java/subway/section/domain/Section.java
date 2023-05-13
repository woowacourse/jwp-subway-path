package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import subway.station.domain.Station;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class Section {
    private final Station left;
    private final Station right;
    private final Distance distance;
    
    public Section(final String left, final String right, final long distance) {
        this(new Station(left), new Station(right), new Distance(distance));
    }
    
    public Section(final Station left, final Station right, final Distance distance) {
        this.left = left;
        this.right = right;
        this.distance = distance;
    }
    
    public Section createLeftSection(final String leftAdditional, final long additionalDistance) {
        return new Section(leftAdditional, left.getName(), additionalDistance);
    }
    
    public Section createRightSection(final String rightAdditional, final long additionalDistance) {
        return new Section(right.getName(), rightAdditional, additionalDistance);
    }
    
    public List<Section> divideSection(final String betweenAdditional, final long additionalDistance) {
        final Section leftSection = new Section(left.getName(), betweenAdditional, additionalDistance);
        final Section rightSection = new Section(betweenAdditional, right.getName(), this.distance.subtract(additionalDistance));
        return List.of(leftSection, rightSection);
    }
}
