package subway.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.dao.StationDao;
import subway.domain.entity.SectionEntity;
import subway.domain.entity.StationEntity;
import subway.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Component
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public Long insert(final StationEntity stationEntity) {
        return stationDao.insert(stationEntity);
    }

    public StationEntity findFinalUpStation(final Long lineId) {
        return stationDao.findFinalUpStation(lineId)
                .orElseThrow(() -> StationNotFoundException.THROW);
    }

    public StationEntity findById(final Long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(() -> StationNotFoundException.THROW);
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

    @Transactional
    public void updateById(final Long stationId, final String name) {
        StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> StationNotFoundException.THROW);
        stationEntity.updateName(name);
        stationDao.updateById(stationId, stationEntity);
    }

    @Transactional
    public void deleteById(final Long stationId) {
        stationDao.deleteById(stationId);
    }

}
