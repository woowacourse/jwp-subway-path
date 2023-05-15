package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Subway;
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

    public void save(final StationSaveRequest request) {
        final Subway subway = getSubway();
        subway.add(
                request.getLineName(),
                request.getBaseStationName(),
                request.getAdditionalStationName(),
                request.getDistance(),
                request.getDirection()
        );
        saveUpdatedLine(subway, request.getLineName());
    }

    public void initialSave(final StationInitialSaveRequest request) {
        final Subway subway = getSubway();
        subway.initialAdd(
                request.getLineName(),
                request.getLeftStationName(),
                request.getRightStationName(),
                request.getDistance()
        );
        saveUpdatedLine(subway, request.getLineName());
    }

    public void delete(final StationDeleteRequest request) {
        final Subway subway = getSubway();
        subway.remove(request.getLineName(), request.getStationName());
        saveUpdatedLine(subway, request.getLineName());
    }

    private Subway getSubway() {
        return new Subway(lineRepository.findAll());
    }

    private void saveUpdatedLine(final Subway subway, final String request) {
        final Line updatedLine = subway.findLineByLineName(request);
        lineRepository.save(updatedLine);
    }
}
