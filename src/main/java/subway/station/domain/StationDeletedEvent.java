package subway.station.domain;

import java.util.Objects;

public class StationDeletedEvent {

    private final long id;

    public StationDeletedEvent(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationDeletedEvent that = (StationDeletedEvent) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
