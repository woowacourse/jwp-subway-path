package subway.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.dao.SectionDao;
import subway.domain.entity.SectionEntity;
import subway.exception.SectionNotFoundException;
import subway.presentation.dto.SectionSaveRequest;

import java.util.List;
import java.util.Optional;

@Component
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional
    public void saveSection(final SectionSaveRequest request) {
        Optional<SectionEntity> sectionOptional = sectionDao.findByUpStationId(request.getUpStationId());
        if (sectionOptional.isPresent()) {
            deleteOriginAndCreateNewSection(request, sectionOptional);
            return;
        }
        SectionEntity sectionEntity = SectionEntity.of(request.getLineId(), request.getUpStationId(), request.getDownStationId(), request.getDistance());
        sectionDao.insert(sectionEntity);
    }

    private void deleteOriginAndCreateNewSection(final SectionSaveRequest request, final Optional<SectionEntity> sectionOptional) {
        SectionEntity sectionEntity = sectionOptional.get();
        int fromUpStationToDownStationDistance = request.getDistance();
        int fromDownStationToUpStationDistance = sectionEntity.getDistance() - request.getDistance();
        SectionEntity fromUpStationToDownStationSectionEntity = SectionEntity.of(request.getLineId(), sectionEntity.getUpStationId(), request.getDownStationId(), fromUpStationToDownStationDistance);
        SectionEntity fromDownStationToUpStationSectionEntity = SectionEntity.of(request.getLineId(), request.getDownStationId(), sectionEntity.getDownStationId(), fromDownStationToUpStationDistance);
        sectionDao.deleteById(sectionEntity.getId());
        sectionDao.insert(fromUpStationToDownStationSectionEntity);
        sectionDao.insert(fromDownStationToUpStationSectionEntity);
    }

    public List<SectionEntity> findAll() {
        return sectionDao.findAll();
    }

    public SectionEntity findLeftSectionByStationId(final Long stationId) {
        return sectionDao.findLeftSectionByStationId(stationId)
                .orElseThrow(() -> SectionNotFoundException.THROW);
    }

    public SectionEntity findRightSectionByStationId(final Long stationId) {
        return sectionDao.findRightSectionByStationId(stationId)
                .orElseThrow(() -> SectionNotFoundException.THROW);
    }

}
