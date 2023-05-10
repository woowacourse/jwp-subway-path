package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.CreationStationDto;
import subway.persistence.dao.StationDao;
import subway.domain.Station;
import subway.persistence.entity.StationEntity;
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
        //final Station station = stationDao.insert(Station.from(stationName));
        return CreationStationDto.from(Station.from("잠실역"));
    }

    public ReadStationResponse findStationResponseById(final Long id) {
        return ReadStationResponse.of(Station.from("잠실역"));
    }

    public List<ReadStationResponse> findAllStationResponses() {
        final List<Station> stations = List.of(Station.from("잠실역"));

        return stations.stream()
                .map(ReadStationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
