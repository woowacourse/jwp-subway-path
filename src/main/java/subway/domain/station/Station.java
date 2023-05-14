package subway.domain.station;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
public class Station {

    private static final int MAX_STATION_NAME_LENGTH = 20;

    private final Long id;
    private StationName name;

    public Station(@Nullable final Long id, final StationName name) {
        this.id = id;
        this.name = name;
    }

    public Station(@Nullable final Long id, final String name) {
        this(id, new StationName(name));
    }

    public Station(final String name) {
        this(null, name);
    }

    public void updateName(final String name) {
        this.name = new StationName(name);
    }
}
