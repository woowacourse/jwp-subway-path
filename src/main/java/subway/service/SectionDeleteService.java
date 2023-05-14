package subway.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.entity.SectionEntity;

@Service
public class SectionDeleteService {

    private final SectionDao sectionDao;

    public SectionDeleteService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void deleteSection(final Long lineId, final Long stationId) {
        final Optional<SectionEntity> upSection = sectionDao.findNeighborUpSection(lineId, stationId);
        final Optional<SectionEntity> downSection = sectionDao.findNeighborDownSection(lineId, stationId);

        validateRegistered(upSection, downSection);
        deleteSection(upSection, downSection);
        if (upSection.isPresent() && downSection.isPresent()) {
            insertNewSection(lineId, upSection.get(), downSection.get());
        }
    }

    private void validateRegistered(final Optional<SectionEntity> upSection, final Optional<SectionEntity> downSection) {
        if (upSection.isEmpty() && downSection.isEmpty()) {
            throw new IllegalArgumentException("등록되어있지 않은 역은 지울 수 없습니다.");
        }
    }

    private void deleteSection(final Optional<SectionEntity> upSection, final Optional<SectionEntity> downSection) {
        upSection.ifPresent(section -> sectionDao.deleteById(section.getId()));
        downSection.ifPresent(section -> sectionDao.deleteById(section.getId()));
    }

    private void insertNewSection(final Long lineId, final SectionEntity upSection, final SectionEntity downSection) {
        final SectionEntity sectionEntity = new SectionEntity(
                lineId,
                upSection.getUpStationId(),
                downSection.getDownStationId(),
                upSection.getDistance() + downSection.getDistance()
        );

        sectionDao.insert(sectionEntity);
    }
}
