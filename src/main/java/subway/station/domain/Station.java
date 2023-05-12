package subway.station.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Station {
    private final StationName name;
    
    public Station(final String name) {
        this(new StationName(name));
    }
    
    public Station(final StationName name) {
        this.name = name;
    }
}
