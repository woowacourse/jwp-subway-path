package subway.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.SectionEntity;
import subway.dto.SectionSaveRequest;

import java.util.List;
import java.util.Optional;

@Component
public class SectionFacade {

    private final SectionDao sectionDao;

    public SectionFacade(final SectionDao sectionDao) {
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
        return sectionDao.findLeftSectionByStationId(stationId);
    }

    public SectionEntity findRightSectionByStationId(final Long stationId) {
        return sectionDao.findRightSectionByStationId(stationId);
    }

}
