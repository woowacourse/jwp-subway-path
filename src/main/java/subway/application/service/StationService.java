package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.domain.Station;
import subway.application.repository.StationRepository;
import subway.application.service.dto.in.IdCommand;
import subway.application.service.dto.in.SaveStationCommand;
import subway.application.service.dto.in.UpdateStationCommand;
import subway.application.service.dto.out.StationResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResult saveStation(SaveStationCommand command) {
        Station station = stationRepository.insert(command.toEntity());
        return new StationResult(station);
    }

    public StationResult findStationResponseById(IdCommand command) {
        return new StationResult(stationRepository.findById(command.getId()));
    }

    public List<StationResult> findAllStationResponses() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(station -> new StationResult(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public void updateStation(UpdateStationCommand command) {
        stationRepository.update(command.toEntity());
    }

    public void deleteStationById(IdCommand command) {
        stationRepository.deleteById(command.getId());
    }
}
