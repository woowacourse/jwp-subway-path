package subway.section.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Section {
    private final String leftStation;
    private final String rightStation;
    private final long distance;
    
    public Section(final String leftStation, final String rightStation, final long distance) {
        this.leftStation = leftStation;
        this.rightStation = rightStation;
        this.distance = distance;
    }
}
