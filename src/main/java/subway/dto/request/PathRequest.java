package subway.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class PathRequest {

    @NotNull
    private final Long sourceId;
    @NotNull
    private final Long destinationId;
    @PositiveOrZero
    private final Integer age;

    public PathRequest(Long sourceId, Long destinationId, Integer age) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.age = age;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public Integer getAge() {
        return age;
    }
}
