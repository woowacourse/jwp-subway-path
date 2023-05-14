package subway.dto;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetAllStationsInLineResponses {

    private final List<GetAllStationsInLineResponse> responses;

    public GetAllStationsInLineResponses(final Map<Line, List<Station>> result) {
        responses = result.entrySet().stream()
                .map(entry -> new GetAllStationsInLineResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<GetAllStationsInLineResponse> getResponses() {
        return responses;
    }
}