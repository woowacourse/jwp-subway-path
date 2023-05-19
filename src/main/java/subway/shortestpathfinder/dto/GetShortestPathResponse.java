package subway.shortestpathfinder.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import subway.shortestpathfinder.domain.ShortestPathResult;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class GetShortestPathResponse {
    private final List<String> shortestPath;
    private final Long shortestDistance;
    private final Long fee;
    
    public GetShortestPathResponse(final ShortestPathResult shortestPathResult) {
        this(shortestPathResult.getShortestPath(), shortestPathResult.getShortestDistance(), shortestPathResult.getFee());
    }
}
