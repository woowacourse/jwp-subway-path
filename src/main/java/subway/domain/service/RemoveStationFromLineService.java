package subway.domain.service;

import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Station;

@Component
public class RemoveStationFromLineService {

    public void remove(final LineRepository lineRepository, final Line line, final Station station) {
        line.removeStation(station);
        if (line.sections().isEmpty()) {
            lineRepository.delete(line);
            return;
        }
        lineRepository.update(line);
    }
}
