package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Section {

    private final Station upstream;
    private final Station downstream;
    private final int distance;

    public Section(Station upstream, Station downstream, int distance) {
        this.upstream = upstream;
        this.downstream = downstream;
        this.distance = distance;
    }
}
