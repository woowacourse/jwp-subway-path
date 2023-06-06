package subway.route.dto.request;

public class RouteFindRequestDto {

    private final Long sourceId;
    private final Long targetId;

    public RouteFindRequestDto(Long sourceId, Long targetId) {
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
