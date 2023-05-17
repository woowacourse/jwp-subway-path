package subway.path.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ShortestPathResult {
    private final List<String> shortestPath;
    private final Long shortestDistance;
    private final Long fee;
}
