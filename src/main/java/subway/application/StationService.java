package subway.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.exception.StationNotFoundException;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station findStationByName(String stationName) {
        return stationRepository.findStationByName(stationName)
                                .orElseThrow(() -> new StationNotFoundException("존재하지 않는 역 이름입니다."));
    }

    public Station findStationById(long stationId) {
        return stationRepository.findStationById(stationId)
                                .orElseThrow(() -> new StationNotFoundException("존재하지 않는 역 ID입니다."));
    }

    public long createStationIfNotExist(String stationName) {
        final Station stationToInsert = new Station(stationName);

        return stationRepository.findIdByName(stationName)
                                .orElseGet(() -> stationRepository.insert(stationToInsert));
    }
}
