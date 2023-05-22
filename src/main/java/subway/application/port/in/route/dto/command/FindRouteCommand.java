package subway.application.port.in.route.dto.command;

public class FindRouteCommand {

    private final long sourceStationId;
    private final long targetStationId;
    private final Integer age;

    public FindRouteCommand(final long sourceStationId, final long targetStationId, final Integer age) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.age = age;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }

    public Integer getAge() {
        return age;
    }
}
