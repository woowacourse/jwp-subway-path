package subway.infrastructure.shortestpath;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import subway.domain.Path;

@Component
public class GraphCache {

    private final Map<Path, LinesGraphAdapter> cache = new ConcurrentHashMap<>();

    public LinesGraphAdapter linesGraphAdapter(final Path path) {
        if (!cache.containsKey(path)) {
            cache.put(path, LinesGraphAdapter.adapt(path));
        }
        return cache.get(path);
    }

    public void updateCache(final Path path) {
        if (!cache.containsKey(path)) {
            cache.put(path, LinesGraphAdapter.adapt(path));
        }
    }
}
