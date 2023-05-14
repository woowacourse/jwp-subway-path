package subway.application.line.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import subway.application.line.port.out.LineRepository;
import subway.domain.line.Line;
import subway.domain.station.event.StationDeletedEvent;

@RequiredArgsConstructor
@Service
public class LineRemoveStationService {

    private final LineRepository lineRepository;

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
