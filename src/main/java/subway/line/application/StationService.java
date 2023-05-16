package subway.line.application;

import static subway.line.exception.station.StationExceptionType.DUPLICATE_STATION_NAME;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.StationCreateCommand;
import subway.line.domain.Station;
import subway.line.domain.StationRepository;
import subway.line.exception.station.StationException;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public UUID create(final StationCreateCommand command) {
        if (stationRepository.findByName(command.name()).isPresent()) {
            throw new StationException(DUPLICATE_STATION_NAME);
        }
        final Station station = command.toDomain();
        stationRepository.save(station);
        return station.id();
    }
}
