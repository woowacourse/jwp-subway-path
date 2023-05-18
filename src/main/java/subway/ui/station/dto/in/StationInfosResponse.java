package subway.ui.station.dto.in;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StationInfosResponse {

    private final List<StationInfoResponse> stations;

    private StationInfosResponse() {
        this(null);
    }
}
