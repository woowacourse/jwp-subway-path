package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long saveStation(final StationRequest request) {
        Station station = new Station(request.getName());
        Station savedStation = stationRepository.save(station);
        return savedStation.getId();
    }

    @Transactional
    public StationResponse findStation(final Long stationId) {
        Station station = stationRepository.findById(stationId);
        return StationResponse.from(station);
    }

    @Transactional
    public void editStation(final Long stationId, final StationRequest request) {
        Station findStation = stationRepository.findById(stationId);
        Station station = new Station(request.getName());
        stationRepository.update(findStation, station);
    }

    @Transactional
    public void removeStation(final Long stationId) {
        Station findStation = stationRepository.findById(stationId);
        stationRepository.delete(findStation);
    }
}
