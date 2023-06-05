package subway.route.application.port.in.find;

public class RouteFindRequestDto {

    private final Long sourceId;
    private final Long targetId;

    public RouteFindRequestDto(final Long sourceId, final Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSource() {
        return sourceId;
    }

    public Long getTarget() {
        return targetId;
    }
}
