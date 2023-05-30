package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public StationService(final StationRepository stationRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Long saveStation(final StationRequest request) {
        Station station = new Station(request.getName());
        Station savedStation = stationRepository.save(station);
        return savedStation.getId();
    }

    @Transactional(readOnly = true)
    public StationResponse findStation(final Long stationId) {
        Station station = stationRepository.findById(stationId);
        return StationResponse.from(station);
    }

    @Transactional
    public void editStation(final Long stationId, final StationRequest request) {
        Station station = new Station(
                stationId,
                request.getName());
        stationRepository.update(station);
    }

    @Transactional
    public void removeStation(final Long stationId) {
        Station findStation = stationRepository.findById(stationId);
        stationRepository.delete(findStation);
    }
}
