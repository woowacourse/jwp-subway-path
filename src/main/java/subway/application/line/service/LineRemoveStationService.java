package subway.application.line.service;

import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.port.out.LineRepository;
import subway.domain.line.Line;
import subway.domain.station.event.StationDeletedEvent;

@Service
@Transactional
public class LineRemoveStationService {

    private final LineRepository lineRepository;

    public LineRemoveStationService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @EventListener(StationDeletedEvent.class)
    public void removeStation(final StationDeletedEvent event) {
        final List<Line> lines = lineRepository.findAll();
        for (final Line line : lines) {
            line.deleteStation(event.getId());
            final Line result = lineRepository.update(line);
            if (result.isEmpty()) {
                lineRepository.deleteById(result.getId());
            }
        }
    }
}
