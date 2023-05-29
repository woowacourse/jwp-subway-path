package subway.business.service.dto;

public class LineSaveRequest {
    private final String name;
    private final Long upwardTerminusId;
    private final Long downwardTerminusId;
    private final Integer distance;
    private final Integer fare;

    public LineSaveRequest(String name, Long upwardTerminusId, Long downwardTerminusId, Integer distance, Integer fare) {
        this.name = name;
        this.upwardTerminusId = upwardTerminusId;
        this.downwardTerminusId = downwardTerminusId;
        this.distance = distance;
        this.fare = fare;
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

    public Integer getDistance() {
        return distance;
    }

    public Integer getFare() {
        return fare;
    }
}
