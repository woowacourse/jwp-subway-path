package subway.service.section.dto;

public class SectionCreateRequest {
    private final String upStationName;
    private final String downStationName;
    private final int distance;

    public SectionCreateRequest(String upStationName, String downStationName, int distance) {
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
