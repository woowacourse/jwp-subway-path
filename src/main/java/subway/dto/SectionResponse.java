package subway.dto;

import subway.domain.Station;

public class SectionResponse {
    private final Long lineId;

    private final String leftStation;
    private final String rightStation;

    public SectionResponse(final Long lineId, final Station leftStation, final Station rightStation) {
        this.lineId = lineId;
        this.leftStation = leftStation.getName();
        this.rightStation = rightStation.getName();
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLeftStation() {
        return leftStation;
    }

    public String getRightStation() {
        return rightStation;
    }
}
