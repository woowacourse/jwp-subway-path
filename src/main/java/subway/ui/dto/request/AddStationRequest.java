package subway.ui.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddStationRequest {
    private final Long firstStationId;
    private final Long secondStationId;
    @NotBlank(message = "추가하려는 역은 필수값 입니다.")
    private final String newStation;
    @NotNull(message = "역 간의 거리는 필수값 입니다.")
    private final Integer distance;

    public AddStationRequest(
            final Long firstStationId,
            final Long secondStationId,
            final String newStation,
            final int distance
    ) {
        this.firstStationId = firstStationId;
        this.secondStationId = secondStationId;
        this.newStation = newStation;
        this.distance = distance;
    }

    public Long getFirstStationId() {
        return firstStationId;
    }

    public Long getSecondStationId() {
        return secondStationId;
    }

    public String getNewStation() {
        return newStation;
    }

    public int getDistance() {
        return distance;
    }
}
