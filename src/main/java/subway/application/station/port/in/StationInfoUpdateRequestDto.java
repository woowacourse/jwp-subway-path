package subway.application.station.port.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StationInfoUpdateRequestDto {

    private final long id;
    private final String name;
}
