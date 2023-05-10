package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.CreationStationDto;
import subway.dto.ReadStationResponse;
import subway.dto.StationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public CreationStationDto saveStation(final String stationName) {
        Station station = stationDao.insert(new Station(stationName));
        return CreationStationDto.from(station);
    }

    public ReadStationResponse findStationResponseById(Long id) {
        return ReadStationResponse.of(stationDao.findById(id));
    }

    public List<ReadStationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(ReadStationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
