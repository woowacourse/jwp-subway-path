package subway.line.domain.interstation;

import java.util.Objects;
import subway.line.domain.interstation.exception.InterStationException;

public class InterStation {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Distance distance;

    public InterStation(Long id,
            Long upStationId,
            Long downStationId,
            Distance distance) {
        validateStations(upStationId, downStationId);
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public InterStation(Long id, InterStation other) {
        this(id, other.upStationId, other.downStationId, other.distance);
    }

    public InterStation(Long id, Long upStationId, Long downStationId, long distance) {
        this(id, upStationId, downStationId, new Distance(distance));
    }

    public InterStation(Long upStationId, Long downStationId, long distance) {
        this(null, upStationId, downStationId, distance);
    }

    private void validateStations(Long upStationId, Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new InterStationException("상행역과 하행역이 같습니다.");
        }
    }

    public boolean contains(Long stationId) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InterStation that = (InterStation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
