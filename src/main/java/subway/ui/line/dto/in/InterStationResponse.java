package subway.ui.line.dto.in;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InterStationResponse {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private long distance;
}
