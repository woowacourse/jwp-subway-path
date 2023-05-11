package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.CreationStationDto;
import subway.domain.Station;
import subway.persistence.repository.StationRepository;
import subway.ui.dto.response.ReadStationResponse;

@Service
public class StationService {

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
        final Station station = stationRepository.findById(id).orElseThrow();

        return ReadStationResponse.of(station);
    }

    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
