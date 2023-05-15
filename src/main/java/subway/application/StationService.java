package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(Long id) {
        return StationResponse.of(stationDao.findById(id)
                .orElseThrow(() -> new DomainException(ExceptionType.UN_EXISTED_STATION)));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        final int updatedRow = stationDao.update(new Station(id, stationRequest.getName()));
        if(updatedRow == 0) {
            throw new DomainException(ExceptionType.UN_EXISTED_STATION);
        }
    }

    public void deleteStationById(Long id) {
        final int deletedRow = stationDao.deleteById(id);
        if(deletedRow == 0) {
            throw new DomainException(ExceptionType.UN_EXISTED_STATION);
        }
    }
}
