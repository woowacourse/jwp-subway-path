package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
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
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        long startStationCount = sectionEntities.stream()
                .filter(it -> it.getStartStationId() == stationId)
                .count();

        long endStationCount = sectionEntities.stream()
                .filter(it -> it.getStartStationId() == stationId)
                .count();

        if (startStationCount == 0 || endStationCount == 0) {
            stationDao.deleteById(stationId);
        }
    }
}