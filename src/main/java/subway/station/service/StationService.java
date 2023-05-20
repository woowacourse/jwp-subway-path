package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.Station;
import subway.station.domain.repository.StationRepository;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }


    @Transactional
    public Long insert(final String name) {
        return stationRepository.insert(Station.from(name));
    }

    public Station findById(final Long stationId) {
        return stationRepository.findById(stationId);
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    @Transactional
    public void updateById(final Long stationId, final String name) {
        stationRepository.updateById(stationId, Station.from(name));
    }

    @Transactional
    public void deleteById(final Long stationId) {
        stationRepository.deleteById(stationId);
    }

    @Transactional
    public void ifFinalStationDelete(final Long lineId, final Long stationId) {
        Station station = findById(stationId);
        Map<String, Station> finalStations = stationRepository.getFinalStations(lineId);
        if (station.isFinalStations(finalStations)) {
            stationRepository.deleteById(stationId);

        }
    }

}
