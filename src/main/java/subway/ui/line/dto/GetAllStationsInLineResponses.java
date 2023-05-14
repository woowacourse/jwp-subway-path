package subway.ui.line.dto;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetAllStationsInLineResponses {

    private final List<GetAllStationsInLineResponse> lines;

    public GetAllStationsInLineResponses(final Map<Line, List<Station>> result) {
        lines = result.entrySet().stream()
                .map(entry -> new GetAllStationsInLineResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<GetAllStationsInLineResponse> getLines() {
        return lines;
    }
}