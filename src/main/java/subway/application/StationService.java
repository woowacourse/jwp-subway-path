package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;

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
        boolean isExistStartStationInSections = sectionDao.existStartStationByStationId(stationId);
        boolean isExistEndStationInSections = sectionDao.existEndStationByStationId(stationId);

        if (!isExistStartStationInSections && !isExistEndStationInSections) {
            stationDao.deleteById(stationId);
        }
    }
}