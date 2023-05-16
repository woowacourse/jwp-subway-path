package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.core.Line;
import subway.domain.core.Subway;
import subway.dto.StationDeleteRequest;
import subway.dto.StationInitialSaveRequest;
import subway.dto.StationSaveRequest;
import subway.repository.LineRepository;

@Transactional
@Service
public class StationService {

    private final LineRepository lineRepository;

    public StationService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void initialSave(final StationInitialSaveRequest request) {
        final Subway subway = new Subway(lineRepository.findAll());
        subway.initialAdd(
                request.getLineName(),
                request.getLeftStationName(),
                request.getRightStationName(),
                request.getDistance()
        );
        saveLine(subway, request.getLineName());
    }

    private void saveLine(final Subway subway, final String name) {
        final Line line = subway.findLineByName(name);
        lineRepository.save(line);
    }

    public void save(final StationSaveRequest request) {
        final Subway subway = new Subway(lineRepository.findAll());
        subway.add(
                request.getLineName(),
                request.getBaseStationName(),
                request.getAdditionalStationName(),
                request.getDistance(),
                request.getDirection()
        );
        saveLine(subway, request.getLineName());
    }

    public void delete(final StationDeleteRequest request) {
        final Subway subway = new Subway(lineRepository.findAll());
        subway.remove(request.getLineName(), request.getStationName());
        saveLine(subway, request.getLineName());
    }
}
