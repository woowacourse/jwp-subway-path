package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.CreationStationDto;
import subway.application.dto.ReadStationDto;
import subway.domain.station.Station;
import subway.persistence.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public CreationStationDto saveStation(final String name) {
        final Station station = Station.from(name);
        final Station persistStation = stationRepository.insert(station);

        return CreationStationDto.from(persistStation);
    }

    public ReadStationDto findStationById(final Long id) {
        final Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        return ReadStationDto.from(station);
    }

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
