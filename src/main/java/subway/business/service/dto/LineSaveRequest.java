package subway.business.service.dto;

public class LineSaveRequest {
    private final String name;
    private final String upwardTerminus;
    private final String downwardTerminus;
    private final int distance;

    public LineSaveRequest(String name, String upwardTerminus, String downwardTerminus, int distance) {
        this.name = name;
        this.upwardTerminus = upwardTerminus;
        this.downwardTerminus = downwardTerminus;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getUpwardTerminus() {
        return upwardTerminus;
    }

    public String getDownwardTerminus() {
        return downwardTerminus;
    }

    public int getDistance() {
        return distance;
    }
}
