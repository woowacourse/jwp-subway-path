package subway.ui.line.dto;

import subway.domain.line.Line;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllStationsInLineResponses {

    private final List<GetAllStationsInLineResponse> lines;

    public GetAllStationsInLineResponses(final List<Line> result) {
        lines = result.stream()
                .map(line -> new GetAllStationsInLineResponse(line, line.stations()))
                .collect(Collectors.toList());
    }

    public List<GetAllStationsInLineResponse> getLines() {
        return lines;
    }
}