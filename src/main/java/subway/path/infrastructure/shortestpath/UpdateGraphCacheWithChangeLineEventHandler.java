package subway.path.infrastructure.shortestpath;

import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.event.ChangeLineEvent;
import subway.path.domain.Path;

@Component
public class UpdateGraphCacheWithChangeLineEventHandler {

    private final LineRepository lineRepository;
    private final GraphCache graphCache;

    public UpdateGraphCacheWithChangeLineEventHandler(
            final LineRepository lineRepository,
            final GraphCache graphCache
    ) {
        this.lineRepository = lineRepository;
        this.graphCache = graphCache;
    }

    @EventListener(classes = {ApplicationReadyEvent.class})
    public void initCache() {
        final List<Line> lines = lineRepository.findAll();
        graphCache.updateCache(new Path(lines));
    }

    @TransactionalEventListener(classes = {ChangeLineEvent.class})
    public void updateCache() {
        final List<Line> lines = lineRepository.findAll();
        graphCache.updateCache(new Path(lines));
    }
}
