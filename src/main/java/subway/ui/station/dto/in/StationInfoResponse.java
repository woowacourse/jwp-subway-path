package subway.ui.station.dto.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationInfoResponse {

    private final long id;
    private final String name;
}
