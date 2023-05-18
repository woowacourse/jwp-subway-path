package subway.ui.station.dto.in;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationInfoResponse {

    private long id;
    private String name;

    private StationInfoResponse() {
    }
}
