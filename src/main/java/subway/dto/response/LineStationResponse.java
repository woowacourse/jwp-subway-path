package subway.dto.response;

public class LineStationResponse {
    private Long id;
    private StationResponse upBoundStation;
    private StationResponse downBoundStation;
    private LineResponse line;
    private Integer distance;

    public LineStationResponse() {
    }

    public LineStationResponse(Long id, StationResponse upBoundStation, StationResponse downBoundStation, LineResponse line, Integer distance) {
        this.id = id;
        this.upBoundStation = upBoundStation;
        this.downBoundStation = downBoundStation;
        this.line = line;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getUpBoundStation() {
        return upBoundStation;
    }

    public StationResponse getDownBoundStation() {
        return downBoundStation;
    }

    public LineResponse getLine() {
        return line;
    }

    public Integer getDistance() {
        return distance;
    }
}
