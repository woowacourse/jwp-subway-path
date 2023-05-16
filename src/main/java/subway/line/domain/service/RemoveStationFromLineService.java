package subway.line.domain.service;

import org.springframework.stereotype.Component;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.Station;

@Component
public class RemoveStationFromLineService {

    private final LineRepository lineRepository;

    public RemoveStationFromLineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void remove(final Line line, final Station station) {
        line.removeStation(station);
        if (line.isEmpty()) {
            lineRepository.delete(line);
            return;
        }
        lineRepository.update(line);
    }
}
