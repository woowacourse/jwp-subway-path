package subway.infrastructure.shortestpath;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Lines;
import subway.domain.event.ChangeLineEvent;

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

    @TransactionalEventListener(classes = {ChangeLineEvent.class})
    public void updateCache() {
        final List<Line> lines = lineRepository.findAll();
        graphCache.updateCache(new Lines(lines));
    }
}
