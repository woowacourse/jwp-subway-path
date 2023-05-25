package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

public class TransferRequest {
    @Schema(description = "환승으로 등록할 첫번째 역의 ID")
    @NonNull
    private final long firstStationId;

    @Schema(description = "환승으로 등록할 두번째 역의 ID")
    @NonNull
    private final long lastStationId;

    public TransferRequest(long firstStationId, long lastStationId) {
        this.firstStationId = firstStationId;
        this.lastStationId = lastStationId;
    }

    public long getFirstStationId() {
        return firstStationId;
    }

    public long getLastStationId() {
        return lastStationId;
    }
}
