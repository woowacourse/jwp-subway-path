package subway.infrastructure.shortestpath;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import subway.domain.Lines;

@Component
public class GraphCache {

    private final Map<Lines, LinesGraphAdapter> cache = new ConcurrentHashMap<>();

    public LinesGraphAdapter linesGraphAdapter(final Lines lines) {
        if (!cache.containsKey(lines)) {
            cache.put(lines, LinesGraphAdapter.adapt(lines));
        }
        return cache.get(lines);
    }

    public void updateCache(final Lines lines) {
        if (!cache.containsKey(lines)) {
            cache.put(lines, LinesGraphAdapter.adapt(lines));
        }
    }
}
