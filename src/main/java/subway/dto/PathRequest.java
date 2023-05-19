package subway.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull(message = "출발 역의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Long sourceId;

    @NotNull(message = "도착 역의 아이디를 입력해 주세요. 입력값 : ${validatedValue}")
    private final Long targetId;

    public PathRequest(final Long sourceId, final Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
