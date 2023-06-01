package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.persistence.dao.StationDao;
import subway.persistence.repository.SubwayRepository;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StationService {
    private final StationDao stationDao;
    private final SubwayRepository subwayRepository;

    public StationService(final StationDao stationDao, final SubwayRepository subwayRepository) {
        this.stationDao = stationDao;
        this.subwayRepository = subwayRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        final Station persist = subwayRepository.addStation(new Station(stationRequest.getName()));
        return StationResponse.of(persist);
    }

    @Transactional(readOnly = true)
    public StationResponse findStationResponseById(Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
