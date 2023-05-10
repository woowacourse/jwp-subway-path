package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.CreationStationDto;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.ui.dto.response.ReadStationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public CreationStationDto saveStation(final String stationName) {
        final Station station = stationDao.insert(new Station(stationName));
        return CreationStationDto.from(station);
    }

    public ReadStationResponse findStationResponseById(final Long id) {
        return ReadStationResponse.of(stationDao.findById(id));
    }

    public List<ReadStationResponse> findAllStationResponses() {
        final List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(ReadStationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
