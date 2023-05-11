package subway.dto;

public class SectionRequest {
    private String upStationName;
    private String downStationName;
    private int distance;

    public SectionRequest() {
    }

    public SectionRequest(final String upStationName, final String downStationName, final int distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }
}
