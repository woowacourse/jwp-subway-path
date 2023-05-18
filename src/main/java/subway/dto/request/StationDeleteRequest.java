package subway.dto.request;

import org.jetbrains.annotations.NotNull;

public class StationDeleteRequest {

    @NotNull("삭제할 역은 공백일 수 없습니다.")
    private Long stationId;

    public StationDeleteRequest() {
    }

    public StationDeleteRequest(@NotNull final Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
