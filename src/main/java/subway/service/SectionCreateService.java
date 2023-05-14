package subway.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.Direction;
import subway.entity.SectionEntity;

@Service
public class SectionCreateService {

    private final SectionDao sectionDao;

    public SectionCreateService(final SectionDao sectionDao) {
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
        if (sectionDao.findByLineId(lineId).isEmpty()) {
            return createSectionWhenNoNeighbor(lineId, baseId, addedId, direction, distance);
        }

        final Optional<SectionEntity> neighborUpSection = sectionDao.findNeighborUpSection(lineId, baseId);
        final Optional<SectionEntity> neighborDownSection = sectionDao.findNeighborDownSection(lineId, baseId);
        if (neighborUpSection.isEmpty() && neighborDownSection.isEmpty()) {
            throw new IllegalArgumentException("기준점이 되는 역은 이미 구간에 존재해야 합니다.");
        }

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
        sectionDao.deleteById(existSectionEntity.getId());

        if (Direction.from(direction) == Direction.UP) {
            final SectionEntity upSectionEntity = new SectionEntity(lineId, existSectionEntity.getUpStationId(), addedId, existSectionEntity.getDistance() - distance);
            final SectionEntity upSavedSectionEntity = sectionDao.insert(upSectionEntity);
            final SectionEntity downSectionEntity = new SectionEntity(lineId, addedId, existSectionEntity.getDownStationId(), distance);
            final SectionEntity downSavedSectionEntity = sectionDao.insert(downSectionEntity);
            return List.of(upSavedSectionEntity, downSavedSectionEntity);
        }
        final SectionEntity upSectionEntity = new SectionEntity(lineId, existSectionEntity.getUpStationId(), addedId, distance);
        final SectionEntity upSavedSectionEntity = sectionDao.insert(upSectionEntity);
        final SectionEntity downSectionEntity = new SectionEntity(lineId, addedId, existSectionEntity.getDownStationId(), existSectionEntity.getDistance() - distance);
        final SectionEntity downSavedSectionEntity = sectionDao.insert(downSectionEntity);
        return List.of(upSavedSectionEntity, downSavedSectionEntity);
    }
}
