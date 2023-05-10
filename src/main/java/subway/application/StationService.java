package subway.application;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.StationCreateCommand;
import subway.domain.StationRepository;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long create(final StationCreateCommand command) {
        if (stationRepository.findByName(command.getName()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 역입니다.");
        }
        return stationRepository.save(command.toDomain());
    }
}
