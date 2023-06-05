package subway.line.application.service;

import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.out.LineRepository;
import subway.line.domain.Line;
import subway.station.domain.StationDeletedEvent;

@Service
@Transactional
public class LineRemoveStationService {

    private final LineRepository lineRepository;

    public LineRemoveStationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @EventListener(StationDeletedEvent.class)
    public void removeStation(StationDeletedEvent event) {
        List<Line> lines = lineRepository.findAll();
        for (Line line : lines) {
            line.deleteStation(event.getId());
            Line result = lineRepository.update(line);
            removeIfNoStationInLine(result);
        }
    }

    private void removeIfNoStationInLine(Line result) {
        if (result.isEmpty()) {
            lineRepository.deleteById(result.getId());
        }
    }
}
