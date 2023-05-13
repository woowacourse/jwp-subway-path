package subway.domain.station;

import java.util.Objects;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class Station {

    private static final int MAX_STATION_NAME_LENGTH = 20;

    private final Long id;
    private final StationName name;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Station station = (Station) o;
        return Objects.equals(getId(), station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
