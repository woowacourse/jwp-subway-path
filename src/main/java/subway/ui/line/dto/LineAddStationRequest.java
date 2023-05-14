package subway.ui.line.dto;

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
    private Long newStationId;
    private long distance;

}
