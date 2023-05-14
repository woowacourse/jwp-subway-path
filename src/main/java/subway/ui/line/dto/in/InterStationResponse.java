package subway.ui.line.dto.in;

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
    private Long upStationId;
    private Long downStationId;
    private long distance;

    public static InterStationResponse from(final InterStation interStation) {
        return new InterStationResponse(interStation.getId(), interStation.getUpStationId(),
            interStation.getDownStationId(), interStation.getDistance().getValue());
    }
}
