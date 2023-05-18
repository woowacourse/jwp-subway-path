package subway.dto.request;

import org.jetbrains.annotations.NotNull;

public class SectionRequest {

    @NotNull("상행선은 비어있을 수 없습니다.")
    private Long upStationId;
    @NotNull("하행선은 비어있을 수 없습니다.")
    private Long downStationId;
    @NotNull("거리는 비어있을 수 없습니다.")
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(
            final Long upStationId,
            final Long downStationId,
            final int distance
    ) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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
}
