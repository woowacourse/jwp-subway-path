package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@Service
public class StationService {
    
    private final StationDao stationDao;
    
    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }
    
    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        final Station station = this.stationDao.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }
    
    @Transactional(readOnly = true)
    public StationResponse findStationResponseById(final Long id) {
        return StationResponse.of(this.stationDao.findById(id));
    }
    
    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        final List<Station> stations = this.stationDao.findAll();
        
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void updateStation(final Long id, final StationRequest stationRequest) {
        this.stationDao.update(new Station(id, stationRequest.getName()));
    }
    
    @Transactional
    public void deleteStationById(final Long id) {
        this.stationDao.deleteById(id);
    }
}