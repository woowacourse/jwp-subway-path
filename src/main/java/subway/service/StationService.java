package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.request.StationCreateRequest;
import subway.exception.DuplicateStationException;
import subway.persistence.repository.StationRepository;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long create(final StationCreateRequest request) {
        if (stationRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateStationException(request.getName());
        }
        return stationRepository.save(request.toDomain());
    }
}
