package subway.application.dto;

import javax.validation.constraints.NotNull;

public class SectionStations {

    @NotNull(message = "노선의 좌측 역의 번호를 입력해주세요.")
    private final Long leftStationId;

    @NotNull(message = "노선의 우측 역의 번호를 입력해주세요.")
    private final Long rightStationId;

    @NotNull(message = "노선의 거리를 입력해주세요.")
    private final Integer distance;

    public SectionStations(final Long leftStationId, final Long rightStationId, final Integer distance) {
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
    }

    public Long getLeftStationId() {
        return leftStationId;
    }

    public Long getRightStationId() {
        return rightStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
