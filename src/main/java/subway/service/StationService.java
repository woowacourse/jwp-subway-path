package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.station.Station;
import subway.exception.DuplicatedStationNameException;
import subway.repository.StationRepository;
import subway.service.dto.StationDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long create(final String name) {
        stationRepository.findByName(name)
                .ifPresent(station -> {
                    throw new DuplicatedStationNameException(station.getName());
                });

        return stationRepository.create(new Station(name));
    }

    @Transactional
    public List<StationDto> findById(final List<Long> ids) {
        return stationRepository.findById(ids).stream()
                .map(StationDto::of)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public List<StationDto> findAll() {
        return stationRepository.findAll().stream()
                .map(StationDto::of)
                .collect(Collectors.toUnmodifiableList());
    }
}
