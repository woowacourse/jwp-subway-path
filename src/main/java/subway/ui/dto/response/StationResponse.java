package subway.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StationResponse {
    private String name;

    public static List<StationResponse> of(List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getName()))
                .collect(Collectors.toList());
    }
}
