package subway.entity;

public class SectionDetailEntity {

    private final long id;
    private final int distance;
    private final long lineId;
    private final String lineName;
    private final String lineColor;
    private final long previousStationId;
    private final String previousStationName;
    private final long nextStationId;
    private final String nextStationName;

    public SectionDetailEntity(final long id, final int distance, final long lineId,
                               final String lineName, final String lineColor,
                               final long previousStationId, final String previousStationName,
                               final long nextStationId, final String nextStationName) {
        this.id = id;
        this.distance = distance;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.previousStationId = previousStationId;
        this.previousStationName = previousStationName;
        this.nextStationId = nextStationId;
        this.nextStationName = nextStationName;
    }

    public long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineColor() {
        return lineColor;
    }

    public long getPreviousStationId() {
        return previousStationId;
    }

    public String getPreviousStationName() {
        return previousStationName;
    }

    public long getNextStationId() {
        return nextStationId;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    @Override
    public String toString() {
        return "SectionDetail{" +
                "id=" + id +
                ", distance=" + distance +
                ", lineId=" + lineId +
                ", lineName='" + lineName + '\'' +
                ", lineColor='" + lineColor + '\'' +
                ", previousStationId=" + previousStationId +
                ", previousStationName='" + previousStationName + '\'' +
                ", nextStationId=" + nextStationId +
                ", nextStationName='" + nextStationName + '\'' +
                '}';
    }
}
