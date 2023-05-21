package subway.line.domain.station.application;

import org.springframework.stereotype.Service;
import subway.line.domain.station.Station;
import subway.line.domain.station.application.dto.StationSavingInfo;
import subway.line.domain.station.application.dto.StationUpdatingInfo;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station saveStation(StationSavingInfo stationSavingInfo) {
        return stationRepository.insert(stationSavingInfo.getName());
    }

    public Station findById(Long id) {
        return stationRepository.findById(id);
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    public void updateStation(Station station, StationUpdatingInfo updatingInfo) {
        station.changeName(updatingInfo.getName());
        stationRepository.update(station);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}