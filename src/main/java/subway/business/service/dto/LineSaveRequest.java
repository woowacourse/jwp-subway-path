package subway.business.service.dto;

public class LineSaveRequest {
    private final String name;
    private final Long upwardTerminusId;
    private final Long downwardTerminusId;
    private final int distance;

    public LineSaveRequest(String name, Long upwardTerminusId, Long downwardTerminusId, int distance) {
        this.name = name;
        this.upwardTerminusId = upwardTerminusId;
        this.downwardTerminusId = downwardTerminusId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public Long getUpwardTerminusId() {
        return upwardTerminusId;
    }

    public Long getDownwardTerminusId() {
        return downwardTerminusId;
    }

    public int getDistance() {
        return distance;
    }
}
