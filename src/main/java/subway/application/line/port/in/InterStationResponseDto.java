package subway.application.line.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InterStationResponseDto {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private long distance;
}
