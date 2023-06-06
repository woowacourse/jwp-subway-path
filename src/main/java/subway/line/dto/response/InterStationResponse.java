package subway.line.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.line.domain.interstation.InterStation;

public class InterStationResponse {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final long distance;

    public InterStationResponse(Long id, Long upStationId, Long downStationId,
            long distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static List<InterStationResponse> from(List<InterStation> interStations) {
        return interStations.stream()
                .map(InterStationResponse::from)
                .collect(Collectors.toList());
    }

    private static InterStationResponse from(InterStation interStation) {
        return new InterStationResponse(interStation.getId(), interStation.getUpStationId(),
                interStation.getDownStationId(), interStation.getDistanceValue());
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

    public long getDistance() {
        return distance;
    }
}
