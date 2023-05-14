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
        if (isEmptyLine(lineId)) {
            return createSectionWhenNoNeighbor(lineId, baseId, addedId, direction, distance);
        }
        validateBaseStationExist(lineId, baseId);

        return createSectionWhenBaseStationExist(lineId, baseId, addedId, direction, distance);
    }

    private boolean isEmptyLine(final Long lineId) {
        return sectionDao.findByLineId(lineId).isEmpty();
    }

    private void validateBaseStationExist(final Long lineId, final Long baseId) {
        final Optional<SectionEntity> neighborUpSection = sectionDao.findNeighborUpSection(lineId, baseId);
        final Optional<SectionEntity> neighborDownSection = sectionDao.findNeighborDownSection(lineId, baseId);
        if (neighborUpSection.isEmpty() && neighborDownSection.isEmpty()) {
            throw new IllegalArgumentException("기준점이 되는 역은 이미 구간에 존재해야 합니다.");
        }
    }

    private List<SectionEntity> createSectionWhenBaseStationExist(final Long lineId, final Long baseId, final Long addedId, final Boolean direction, final Integer distance) {
        final Optional<SectionEntity> section = sectionDao.findNeighborSection(lineId, baseId, Direction.from(direction));
        if (section.isEmpty()) {
            return createSectionWhenNoNeighbor(lineId, baseId, addedId, direction, distance);
        }
        final SectionEntity existSectionEntity = section.get();
        validateDistance(distance, existSectionEntity);

        return divideSectionByAddedStation(lineId, addedId, direction, distance, existSectionEntity);
    }

    private void validateDistance(final Integer distance, final SectionEntity existSectionEntity) {
        if (existSectionEntity.getDistance() <= distance) {
            throw new IllegalArgumentException("새롭게 등록하는 구간의 거리는 기존에 존재하는 구간의 거리보다 작아야합니다.");
        }
    }

    private List<SectionEntity> createSectionWhenNoNeighbor(final Long lineId, final Long baseId, final Long addedId, final Boolean direction, final Integer distance) {
        final SectionEntity sectionEntity = createSectionEntityByDirection(lineId, baseId, addedId, direction, distance);
        final SectionEntity savedSectionEntity = sectionDao.insert(sectionEntity);
        return List.of(savedSectionEntity);
    }

    private SectionEntity createSectionEntityByDirection(final Long lineId, final Long baseId, final Long addedId, final Boolean direction, final Integer distance) {
        if (Direction.from(direction) == Direction.UP) {
            return new SectionEntity(lineId, addedId, baseId, distance);
        }
        if (Direction.from(direction) == Direction.DOWN) {
            return new SectionEntity(lineId, baseId, addedId, distance);
        }
        throw new IllegalArgumentException("존재하지 않는 방향입니다.");
    }

    private List<SectionEntity> divideSectionByAddedStation(final Long lineId, final Long addedId, final Boolean direction, final Integer distance, final SectionEntity existSectionEntity) {
        sectionDao.deleteById(existSectionEntity.getId());

        final SectionEntity upSectionEntity = createUpSectionByDirection(lineId, addedId, direction, distance, existSectionEntity);
        final SectionEntity upSavedSectionEntity = sectionDao.insert(upSectionEntity);
        final SectionEntity downSectionEntity = createDownSectionByDirection(lineId, addedId, direction, distance, existSectionEntity);
        final SectionEntity downSavedSectionEntity = sectionDao.insert(downSectionEntity);
        return List.of(upSavedSectionEntity, downSavedSectionEntity);
    }

    private SectionEntity createUpSectionByDirection(final Long lineId, final Long addedId, final Boolean direction, final Integer distance, final SectionEntity existSection) {
        if (Direction.from(direction) == Direction.UP) {
            return new SectionEntity(lineId, existSection.getUpStationId(), addedId, existSection.getDistance() - distance);
        }
        if (Direction.from(direction) == Direction.DOWN) {
            return new SectionEntity(lineId, existSection.getUpStationId(), addedId, distance);
        }
        throw new IllegalArgumentException("존재하지 않는 방향입니다.");
    }

    private SectionEntity createDownSectionByDirection(final Long lineId, final Long addedId, final Boolean direction, final Integer distance, final SectionEntity existSection) {
        if (Direction.from(direction) == Direction.UP) {
            return new SectionEntity(lineId, addedId, existSection.getDownStationId(), distance);
        }
        if (Direction.from(direction) == Direction.DOWN) {
            return new SectionEntity(lineId, addedId, existSection.getDownStationId(), existSection.getDistance() - distance);
        }
        throw new IllegalArgumentException("존재하지 않는 방향입니다.");
    }
}
