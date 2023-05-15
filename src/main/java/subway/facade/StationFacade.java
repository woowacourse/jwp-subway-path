package subway.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.SectionEntity;
import subway.domain.StationEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class StationFacade {

    private final StationDao stationDao;

    public StationFacade(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public Long insert(final StationEntity stationEntity) {
        return stationDao.insert(stationEntity);
    }

    public StationEntity findFinalUpStation(final Long lineId) {
        return stationDao.findFinalUpStation(lineId);
    }

    public StationEntity findById(final Long stationId) {
        return stationDao.findById(stationId);
    }

    @Transactional
    public void deleteById(final Long stationId) {
        stationDao.deleteById(stationId);
    }

    public List<StationEntity> findAll(final Long lineId, final List<SectionEntity> sectionEntities) {
        StationEntity finalUpStationEntity = findFinalUpStation(lineId);

        List<StationEntity> results = new ArrayList<>();
        results.add(finalUpStationEntity);

        Long beforeStationId = finalUpStationEntity.getId();

        int totalSections = sectionEntities.size();
        while (results.size() < totalSections + 1) {
            for (SectionEntity sectionEntity : sectionEntities) {
                beforeStationId = IfSameStationUpdateStationId(results, beforeStationId, sectionEntity);
            }
        }
        return results;
    }

    private Long IfSameStationUpdateStationId(final List<StationEntity> results, Long beforeStationId, final SectionEntity sectionEntity) {
        if (sectionEntity.getUpStationId() == beforeStationId) {
            StationEntity stationEntity = findById(sectionEntity.getDownStationId());
            results.add(stationEntity);
            beforeStationId = stationEntity.getId();
        }
        return beforeStationId;
    }

}
