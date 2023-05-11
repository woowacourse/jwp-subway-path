package subway.ui.section.dto;

public class SectionCreateRequest {
    private String upStationName;
    private String downStationName;
    private int distance;

    public SectionCreateRequest(String upStationName, String downStationName, int distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    private SectionCreateRequest() {

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
