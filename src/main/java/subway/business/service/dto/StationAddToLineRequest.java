package subway.business.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StationAddToLineRequest {
    @Schema(description = "노선에 추가할 역의 이름")
    private final String station;

    @Schema(description = "추가할 역과 이웃한 역의 이름")
    private final String neighborhoodStation;

    @Schema(description = "이웃한 역을 기준으로 추가할 방향 (\"상행\", \"하행\")")
    private final String addDirection;

    @Schema(description = "이웃역과 추가할 역 사이의 거리")
    private final Integer distance;

    public StationAddToLineRequest(String station, String neighborhoodStation, String addDirection, Integer distance) {
        this.station = station;
        this.neighborhoodStation = neighborhoodStation;
        this.addDirection = addDirection;
        this.distance = distance;
    }

    public String getStation() {
        return station;
    }

    public String getNeighborhoodStation() {
        return neighborhoodStation;
    }

    public String getAddDirection() {
        return addDirection;
    }

    public Integer getDistance() {
        return distance;
    }
}
