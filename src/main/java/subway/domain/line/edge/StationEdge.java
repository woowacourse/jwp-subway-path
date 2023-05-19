package subway.domain.line.edge;

import subway.exception.IllegalStationEdgeException;

import java.util.Objects;
import java.util.Set;

public class StationEdge {

    private static final int DISTANCE_LOW_BOUNDARY = 0;
    public static final String SAME_STATION_ID_EXCEPTION_MESSAGE = "상행역과 하행역의 거리가 달라야 합니다.";
    public static final String INVALID_DISTANCE_EXCEPTION_MESSAGE = "역간 거리가 올바르지 않습니다.";

    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    public StationEdge(final Long upStationId, final Long downStationId, final int distance) {
        validateStationIds(upStationId, downStationId);
        validateDistance(distance);
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    private void validateStationIds(final Long upStationId, final Long downStationId) {
        if (upStationId.equals(downStationId)) {
            throw new IllegalStationEdgeException(SAME_STATION_ID_EXCEPTION_MESSAGE);
        }
    }

    private void validateDistance(final int distance) {
        if (distance <= DISTANCE_LOW_BOUNDARY) {
            throw new IllegalStationEdgeException(INVALID_DISTANCE_EXCEPTION_MESSAGE);
        }
    }

    public Set<StationEdge> splitFromUp(final Long insertedStationId, final int distanceFromUpStation) {
        final int distanceFromDownStation = distance - distanceFromUpStation;
        return Set.of(
                new StationEdge(upStationId, insertedStationId, distanceFromUpStation),
                new StationEdge(insertedStationId, downStationId, distanceFromDownStation)
        );
    }

    public Set<StationEdge> splitFromDown(final Long insertedStationId, final int distanceFromDownStation) {
        final int distanceFromUpStation = distance - distanceFromDownStation;
        return Set.of(
                new StationEdge(upStationId, insertedStationId, distanceFromUpStation),
                new StationEdge(insertedStationId, downStationId, distanceFromDownStation)
        );
    }

    public boolean isUpStationId(final Long stationId) {
        return upStationId.equals(stationId);
    }

    public boolean isDownStationId(final Long stationId) {
        return downStationId.equals(stationId);
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StationEdge that = (StationEdge) o;
        return Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationId, downStationId);
    }
}
