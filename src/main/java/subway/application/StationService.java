package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.exception.station.InvalidDeleteStationException;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public StationService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void deleteStation(Long stationId) {
        boolean isExistStartStationInSections = sectionDao.existStationByStationId(stationId);

        if (isExistStartStationInSections) {
            throw new InvalidDeleteStationException("구간에 등록되어 있는 역은 삭제할 수 없습니다.");
        }
        stationDao.deleteById(stationId);
    }
}