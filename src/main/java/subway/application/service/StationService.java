package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.domain.Station;
import subway.application.repository.StationRepository;
import subway.application.service.command.in.IdCommand;
import subway.application.service.command.in.SaveStationCommand;
import subway.application.service.command.in.UpdateStationCommand;
import subway.presentation.dto.StationRequest;
import subway.presentation.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(SaveStationCommand command) {
        Station station = stationRepository.insert(command.toEntity());
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(IdCommand command) {
        return StationResponse.of(stationRepository.findById(command.getId()));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(UpdateStationCommand command) {
        stationRepository.update(command.toEntity());
    }

    public void deleteStationById(IdCommand command) {
        stationRepository.deleteById(command.getId());
    }
}
