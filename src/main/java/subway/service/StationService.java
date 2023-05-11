package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.StationRepository;
import subway.exception.DuplicateStationException;
import subway.service.dto.StationCreateCommand;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long create(final StationCreateCommand command) {
        if (stationRepository.findByName(command.getName()).isPresent()) {
            throw new DuplicateStationException(command.getName());
        }
        return stationRepository.save(command.toDomain());
    }
}
