package subway.application.line.port.in;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.interstation.InterStation;

@Getter
@AllArgsConstructor
public class InterStationResponseDto {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private long distance;

    public static List<InterStationResponseDto> from(final List<InterStation> interStations) {
        return interStations.stream()
            .map(InterStationResponseDto::from)
            .collect(Collectors.toList());
    }

    private static InterStationResponseDto from(final InterStation interStation) {
        return new InterStationResponseDto(interStation.getId(), interStation.getUpStationId(),
            interStation.getDownStationId(), interStation.getDistance().getValue());
    }
}
