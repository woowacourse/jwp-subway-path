package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if(stationDao.findByName(stationRequest.getName()).isPresent()) {
            throw new InvalidDataException("이미 존재하는 역입니다.");
        }

        Station station = stationDao.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public StationResponse findStationResponseById(Long id) {
        Station station = stationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 역이 존재하지 않습니다."));
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 역이 존재하지 않습니다."));
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 역이 존재하지 않습니다."));
        stationDao.deleteById(id);
    }
}
