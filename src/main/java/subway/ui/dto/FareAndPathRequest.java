package subway.ui.dto;

import javax.validation.constraints.Positive;

public class FareAndPathRequest {

    @Positive
    private final Long sourceStationId;
    @Positive
    private final Long targetStationId;
    @Positive
    private final int age;

    public FareAndPathRequest(Long sourceStationId, Long targetStationId, int age) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.age = age;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public int getAge() {
        return age;
    }
}
