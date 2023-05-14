package subway.application.station.port.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationInfoResponseDto {

    private final long id;
    private final String name;

}
