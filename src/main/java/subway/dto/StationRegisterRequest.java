package subway.dto;

public class StationRegisterRequest {

    private String where;
    private Long stationId;
    private Long baseId;
    private Integer distance;

    public StationRegisterRequest(String where, Long stationId, Long baseId, Integer distance) {
        this.where = where;
        this.stationId = stationId;
        this.baseId = baseId;
        this.distance = distance;
    }

    public String getWhere() {
        return where;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getBaseId() {
        return baseId;
    }

    public Integer getDistance() {
        return distance;
    }
}
