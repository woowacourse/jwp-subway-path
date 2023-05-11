package subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse from(final Line line) {
        final List<StationResponse> stationResponses = line.getStations()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }
}
