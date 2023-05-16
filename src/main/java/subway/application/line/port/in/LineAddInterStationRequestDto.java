package subway.application.line.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LineAddInterStationRequestDto {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long newStationId;
    private final long distance;
}
