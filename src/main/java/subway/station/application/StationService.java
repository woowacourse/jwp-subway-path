package subway.station.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.Station;
import subway.station.exception.StationNotFoundException;
import subway.station.repository.StationRepository;

@Transactional
@Service
public class StationService {

    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public Station findStationById(long stationId) {
        return stationRepository.findStationById(stationId)
                                .orElseThrow(() -> new StationNotFoundException("존재하지 않는 역 ID입니다."));
    }

    public Station createStationIfNotExist(String stationName) {
        final Station stationToInsert = new Station(stationName);

        return stationRepository.findStationByName(stationName)
                                .orElseGet(() -> stationRepository.createStation(stationToInsert));
    }
}
