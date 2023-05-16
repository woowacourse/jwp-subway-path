package subway.station.adapter.output.persistence;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class StationEntity {
    private final Long id;
    private final String name;
    
    public StationEntity(final String name) {
        this(null, name);
    }
}
