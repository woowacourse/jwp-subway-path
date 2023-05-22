package subway.application.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import subway.application.core.domain.Station;
import subway.application.core.service.dto.in.IdCommand;
import subway.application.core.service.dto.in.SaveStationCommand;
import subway.application.core.service.dto.in.UpdateStationCommand;
import subway.application.core.service.dto.out.StationResult;
import subway.application.port.StationRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResult saveStation(@Valid SaveStationCommand command) {
        Station station = stationRepository.insert(command.toEntity());
        return new StationResult(station);
    }

    public StationResult findStationById(@Valid IdCommand command) {
        return new StationResult(stationRepository.findById(command.getId()));
    }

    public List<StationResult> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(station -> new StationResult(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public void updateStation(@Valid UpdateStationCommand command) {
        stationRepository.update(command.toEntity());
    }

    public void deleteStationById(@Valid IdCommand command) {
        stationRepository.deleteById(command.getId());
    }
}
