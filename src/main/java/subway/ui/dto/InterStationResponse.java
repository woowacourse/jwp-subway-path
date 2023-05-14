package subway.ui.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.interstation.InterStation;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterStationResponse {

    private Long id;
    private Long frontStationId;
    private Long backStationId;
    private long distance;

    public static InterStationResponse from(final InterStation interStation) {
        return new InterStationResponse(interStation.getId(), interStation.getUpStation().getId(),
            interStation.getDownStation().getId(), interStation.getDistance().getValue());
    }
}
