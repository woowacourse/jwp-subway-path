package subway.domain.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.section.dao.SectionDao;
import subway.domain.section.domain.Direction;
import subway.domain.section.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

@Service
public class CreateSectionService {

    private final SectionDao sectionDao;

    public CreateSectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Transactional
    public List<SectionEntity> createSection(
            final Long lineId,
            final Long baseId,
            final Long addedId,
            final Boolean direction,
            final Integer distance
    ) {
        final Optional<SectionEntity> section = sectionDao.findNeighborSection(lineId, baseId, Direction.from(direction));

        if (section.isEmpty()) {
            return createSectionWhenNoNeighbor(lineId, baseId, addedId, direction, distance);
        }

        final SectionEntity existSectionEntity = section.get();

        if (existSectionEntity.getDistance() <= distance) {
            throw new IllegalArgumentException("새롭게 등록하는 구간의 거리는 기존에 존재하는 구간의 거리보다 작아야합니다.");
        }

        return divideSectionByAddedStation(lineId, addedId, direction, distance, existSectionEntity);
    }

    private List<SectionEntity> createSectionWhenNoNeighbor(final Long lineId, final Long baseId, final Long addedId, final Boolean direction, final Integer distance) {
        if (Direction.from(direction) == Direction.UP) {
            final SectionEntity newSectionEntity = new SectionEntity(lineId, addedId, baseId, distance);
            final SectionEntity savedSectionEntity = sectionDao.insert(newSectionEntity);
            return List.of(savedSectionEntity);
        }
        final SectionEntity newSectionEntity = new SectionEntity(lineId, baseId, addedId, distance);
        final SectionEntity savedSectionEntity = sectionDao.insert(newSectionEntity);
        return List.of(savedSectionEntity);
    }

    private List<SectionEntity> divideSectionByAddedStation(final Long lineId, final Long addedId, final Boolean direction, final Integer distance, final SectionEntity existSectionEntity) {
        if (Direction.from(direction) == Direction.UP) {
            final SectionEntity upSectionEntity = new SectionEntity(lineId, existSectionEntity.getUpStationId(), addedId, existSectionEntity.getDistance() - distance);
            sectionDao.updateStationInSection(upSectionEntity);
            final SectionEntity downSectionEntity = new SectionEntity(lineId, addedId, existSectionEntity.getDownStationId(), distance);
            final SectionEntity downSavedSectionEntity = sectionDao.insert(downSectionEntity);
            return List.of(upSectionEntity, downSavedSectionEntity);
        }
        final SectionEntity upSectionEntity = new SectionEntity(lineId, existSectionEntity.getUpStationId(), addedId, distance);
        sectionDao.updateStationInSection(upSectionEntity);
        final SectionEntity downSectionEntity = new SectionEntity(lineId, addedId, existSectionEntity.getDownStationId(), existSectionEntity.getDistance() - distance);
        final SectionEntity downSavedSectionEntity = sectionDao.insert(downSectionEntity);
        return List.of(upSectionEntity, downSavedSectionEntity);
    }
}
