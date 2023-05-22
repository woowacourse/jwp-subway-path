package subway.dto;

public class SectionRequest {

    long lineId;
    long newStationId;
    long baseStationId;
    String direction;
    int distance;

    public SectionRequest() {
    }

    public SectionRequest(final long lineId, final long newStationId, final long baseStationId, final String direction,
                          final int distance) {
        this.lineId = lineId;
        this.newStationId = newStationId;
        this.baseStationId = baseStationId;
        this.direction = direction;
        this.distance = distance;
    }

//    public SectionRequest(final long lineId, final long downStation, final long upStation,
//                          final int distance) {
//        this.lineId = lineId;
//        this.newStationId = downStation;
//        this.baseStationId = upStation;
//        this.direction = null;
//        this.distance = distance;
//    }

    public long getLineId() {
        return this.lineId;
    }

    public long getNewStationId() {
        return this.newStationId;
    }

    public long getBaseStationId() {
        return this.baseStationId;
    }

    public String getDirection() {
        return this.direction;
    }

    public int getDistance() {
        return this.distance;
    }
}