package subway.ui.line.dto.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineAddStationRequest {

    private Long upStationId;
    private Long downStationId;
    @NotNull
    @Positive
    private Long newStationId;
    @Positive
    @NotNull
    private Long distance;

}
