package subway.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    @Autowired
    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station findStationByName(String stationName) {
        return null;
    }

    public long createStationIfNotExist(String stationName) {
        return 0;
    }

    public Station findStationById(long stationId) {
        return null;
    }
}
