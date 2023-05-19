package subway.domain.interstation;

import java.util.Objects;
import subway.domain.interstation.exception.InterStationException;

public class InterStation {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Distance distance;

    public InterStation(final Long id,
            final Long upStationId,
            final Long downStationId,
            final Distance distance) {
        validateStations(upStationId, downStationId);
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public InterStation(final Long id, final InterStation other) {
        this(id, other.upStationId, other.downStationId, other.distance);
    }

    public InterStation(final Long upStationId, final Long downStationId, final Distance distance) {
        this(null, upStationId, downStationId, distance);
    }

    public InterStation(final Long id, final Long upStationId, final Long downStationId, final long distance) {
        this(id, upStationId, downStationId, new Distance(distance));
    }

    public InterStation(final Long upStationId, final Long downStationId, final long distance) {
        this(null, upStationId, downStationId, distance);
    }


    private void validateStations(final Long upStationId, final Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new InterStationException("상행역과 하행역이 같습니다.");
        }
    }

    public boolean contains(final Long stationId) {
        if (stationId == null) {
            return false;
        }
        return upStationId.equals(stationId) || downStationId.equals(stationId);
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public long getDistanceValue() {
        return distance.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InterStation that = (InterStation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
