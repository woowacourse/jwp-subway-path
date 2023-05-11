package subway.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.lang.NonNull;

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
public class SectionLastDeleteRequest {

    @NonNull
    private Long lineId;
    @NonNull
    private Long upwardId;
    @NonNull
    private Long downwardId;

    public SectionLastDeleteRequest(Long lineId, Long upwardId, Long downwardId) {
        this.lineId = lineId;
        this.upwardId = upwardId;
        this.downwardId = downwardId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpwardId() {
        return upwardId;
    }

    public Long getDownwardId() {
        return downwardId;
    }
}
