package subway.domain.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.section.dao.SectionDao;
import subway.domain.section.domain.Direction;
import subway.domain.section.entity.SectionEntity;

import java.util.Optional;

@Service
public class DeleteSectionService {

    private final SectionDao sectionDao;

    public DeleteSectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        final Optional<SectionEntity> upSection = sectionDao.findNeighborSection(lineId, stationId, Direction.UP);
        final Optional<SectionEntity> downSection = sectionDao.findNeighborSection(lineId, stationId, Direction.DOWN);

        if (upSection.isEmpty() && downSection.isEmpty()) {
            throw new IllegalArgumentException("등록되어있지 않은 역은 지울 수 없습니다.");
        }

        if (upSection.isEmpty()) {
            sectionDao.deleteById(downSection.get().getId());
            return;
        }

        if (downSection.isEmpty()) {
            sectionDao.deleteById(upSection.get().getId());
            return;
        }

        final SectionEntity existUpSectionEntity = upSection.get();
        final SectionEntity existDownSectionEntity = downSection.get();

        sectionDao.deleteById(existUpSectionEntity.getId());
        sectionDao.deleteById(existDownSectionEntity.getId());

        final SectionEntity sectionEntity = new SectionEntity(
                lineId,
                existUpSectionEntity.getUpStationId(),
                existDownSectionEntity.getDownStationId(),
                existUpSectionEntity.getDistance() + existDownSectionEntity.getDistance()
        );

        sectionDao.insert(sectionEntity);
    }
}
