package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.exception.DuplicatedStationNameException;
import subway.exception.StationNotFoundException;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long create(final String name) {
        stationRepository.findByName(name)
                .ifPresent(station -> {
                    throw new DuplicatedStationNameException(station.getName());
                });

        return stationRepository.create(new Station(name));
    }

    public Station findById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<Station> findById(List<Long> ids) {
        return stationRepository.findById(ids);
    }
    public List<Station> findAll() {
        return stationRepository.findAll();
    }
}
