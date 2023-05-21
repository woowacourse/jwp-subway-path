package subway.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.exception.StationNotFoundException;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station saveStation(Station stationToSave) {
        return stationDao.insert(stationToSave);
    }

    public Station findStationById(Long stationId) {
        return stationDao.selectById(stationId)
                .orElseThrow(() -> new StationNotFoundException("역 ID에 해당하는 역이 존재하지 않습니다."));
    }

    public Station findByStationNameAndLineName(String stationName, String lineName) {
        return stationDao.selectByStationNameAndLineName(stationName, lineName)
                .orElseThrow(() -> new StationNotFoundException("역 이름과 노선 이름에 해당하는 역이 존재하지 않습니다."));
    }

    public List<Station> findAllStationByLineId(Long lineId) {
        return stationDao.selectAllByLineId(lineId);
    }

    public List<Station> findAllStation() {
        return stationDao.selectAll();
    }

    public void removeStationById(Long stationId) {
        if (stationDao.isNotExistById(stationId)) {
            throw new StationNotFoundException("역 ID에 해당하는 역이 존재하지 않습니다.");
        }
        stationDao.deleteById(stationId);
    }
}
