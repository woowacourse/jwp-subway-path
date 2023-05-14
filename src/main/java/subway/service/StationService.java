package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.exception.DuplicatedStationNameException;
import subway.repository.StationRepository;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long create(final String name) {
        stationRepository.findByName(name)
                .ifPresent(station -> {
                    throw new DuplicatedStationNameException(station.getName());
                });

        return stationRepository.create(new Station(name));
    }

    public List<Station> findById(final List<Long> ids) {
        return stationRepository.findById(ids);
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }
}
