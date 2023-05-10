package subway.dao.entity;

public class SectionEntity {
    private Long id;
    private Long lineId;
    private String startStationName;
    private String endStationName;
    private int distance;

    public SectionEntity() {
    }

    public SectionEntity(Long id, Long lineId, String startStationName, String endStationName, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public int getDistance() {
        return distance;
    }
}
