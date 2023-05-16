package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.StationCreateRequest;
import subway.controller.dto.StationResponse;
import subway.domain.station.Station;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long createStation(final StationCreateRequest request) {
        final Station station = new Station(request.getName());
        return stationRepository.save(station).getId();
    }

    public StationResponse findStationById(final Long stationId) {
        final Station station = stationRepository.findById(stationId);
        return StationResponse.from(station);
    }
}
