package subway.line.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import subway.line.domain.interstation.InterStation;

public class InterStationResponseDto {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final long distance;

    public InterStationResponseDto(Long id, Long upStationId, Long downStationId,
            long distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static List<InterStationResponseDto> from(List<InterStation> interStations) {
        return interStations.stream()
                .map(InterStationResponseDto::from)
                .collect(Collectors.toList());
    }

    private static InterStationResponseDto from(InterStation interStation) {
        return new InterStationResponseDto(interStation.getId(), interStation.getUpStationId(),
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
