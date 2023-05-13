package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.CreationStationDto;
import subway.domain.station.Station;
import subway.persistence.repository.StationRepository;
import subway.ui.dto.response.ReadStationResponse;

@Transactional
@Service
public class StationService {

    private static final String NOT_EXISTS_STATION = "존재하지 않는 역입니다.";

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public CreationStationDto saveStation(final String name) {
        final Station station = Station.of(name);
        final Station persistStation = stationRepository.insert(station);

        return CreationStationDto.from(persistStation);
    }

    public ReadStationResponse findStationById(final Long id) {
        final Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_STATION));

        return ReadStationResponse.of(station);
    }

    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
