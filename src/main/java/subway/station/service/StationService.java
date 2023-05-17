package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.dto.StationCreateDto;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long create(final StationCreateDto stationCreateDto) {
        final Station station = stationRepository.createStation(stationCreateDto.getName());
        return station.getId();
    }
}
