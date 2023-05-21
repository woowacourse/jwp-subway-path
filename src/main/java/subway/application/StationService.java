package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.station.StationCreateDto;
import subway.application.dto.station.StationUpdateDto;
import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.exception.IllegalStationException;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station saveStation(StationCreateDto requestedStation) {
        Station station = new Station(requestedStation.getName());
        if (stationRepository.isDuplicateStation(station)) {
            throw new IllegalStationException("이미 존재하는 역입니다.");
        }
        long savedId = stationRepository.save(station);
        return new Station(savedId, station.getName());
    }

    @Transactional(readOnly = true)
    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    public Station updateStation(StationUpdateDto requestedStation) {
        return stationRepository.update(new Station(requestedStation.getId(), requestedStation.getName()));
    }

    public void deleteStationById(long id) {
        Station station = stationRepository.findById(id);
        stationRepository.delete(station);
    }
}
