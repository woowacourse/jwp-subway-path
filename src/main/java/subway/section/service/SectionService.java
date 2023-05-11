package subway.section.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.dao.SectionDao;
import subway.section.domain.Direction;
import subway.section.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SectionService {

    private final SectionDao sectionDao;

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
