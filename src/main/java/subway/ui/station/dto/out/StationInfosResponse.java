package subway.ui.station.dto.out;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StationInfosResponse {

    private final List<StationInfoResponse> stations;
}
