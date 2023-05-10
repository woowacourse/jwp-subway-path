package subway.application;


import static subway.exception.station.StationExceptionType.DUPLICATE_STATION_NAME;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.StationCreateCommand;
import subway.domain.StationRepository;
import subway.exception.station.StationException;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long create(final StationCreateCommand command) {
        if (stationRepository.findByName(command.name()).isPresent()) {
            throw new StationException(DUPLICATE_STATION_NAME);
        }
        return stationRepository.save(command.toDomain());
    }
}
