package subway.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.graph.Direction;
import subway.domain.section.Section;
import subway.dto.SectionCreateRequest;

@Transactional
@Service
public class SectionCreateService {

    private final SectionDao sectionDao;

    public SectionCreateService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> createSection(final SectionCreateRequest sectionCreateRequest) {
        return createSection(
                sectionCreateRequest.getLineId(),
                sectionCreateRequest.getBaseStationId(),
                sectionCreateRequest.getAddedStationId(),
                sectionCreateRequest.getDirection(),
                sectionCreateRequest.getDistance()
        );
    }

    private List<Section> createSection(
            final Long lineId,
            final Long baseId,
            final Long addedId,
            final Boolean direction,
            final Integer distance
    ) {
        if (isEmptyLine(lineId)) {
            return createSectionWhenNoNeighbor(lineId, baseId, addedId, direction, distance);
        }
        final Optional<Section> upSection = sectionDao.findUpSection(lineId, baseId);
        final Optional<Section> downSection = sectionDao.findDownSection(lineId, baseId);
        validateBaseStationExist(upSection, downSection);

        final Optional<Section> section = getSectionByDirection(upSection, downSection, direction);
        return createSectionWhenBaseStationExist(lineId, baseId, addedId, direction, distance, section);
    }

    private boolean isEmptyLine(final Long lineId) {
        return sectionDao.findByLineId(lineId).isEmpty();
    }

    private List<Section> createSectionWhenNoNeighbor(final Long lineId, final Long baseId, final Long addedId, final Boolean direction, final Integer distance) {
        final Section section = createSectionByDirection(lineId, baseId, addedId, direction, distance);
        final Section savedSection = sectionDao.insert(section);
        return List.of(savedSection);
    }

    private Section createSectionByDirection(final Long lineId, final Long baseId, final Long addedId, final Boolean direction, final Integer distance) {
        if (Direction.from(direction) == Direction.UP) {
            return Section.builder()
                    .lineId(lineId)
                    .upStation(addedId)
                    .downStation(baseId)
                    .distance(distance)
                    .build();
        }
        if (Direction.from(direction) == Direction.DOWN) {
            return Section.builder()
                    .lineId(lineId)
                    .upStation(baseId)
                    .downStation(addedId)
                    .distance(distance)
                    .build();
        }
        throw new IllegalArgumentException("존재하지 않는 방향입니다.");
    }

    private void validateBaseStationExist(final Optional<Section> upSection, final Optional<Section> downSection) {
        if (upSection.isEmpty() && downSection.isEmpty()) {
            throw new IllegalArgumentException("기준점이 되는 역은 이미 구간에 존재해야 합니다.");
        }
    }

    private Optional<Section> getSectionByDirection(final Optional<Section> upSection, final Optional<Section> downSection, final Boolean direction) {
        if (Direction.from(direction) == Direction.UP) {
            return upSection;
        }
        if (Direction.from(direction) == Direction.DOWN) {
            return downSection;
        }
        throw new IllegalArgumentException("존재하지 않는 방향입니다.");
    }

    private List<Section> createSectionWhenBaseStationExist(
            final Long lineId,
            final Long baseId,
            final Long addedId,
            final Boolean direction,
            final Integer distance,
            final Optional<Section> section
    ) {
        if (section.isEmpty()) {
            return createSectionWhenNoNeighbor(lineId, baseId, addedId, direction, distance);
        }

        final Section existSection = section.get();
        validateDistance(distance, existSection);

        return divideSectionByAddedStation(lineId, addedId, direction, distance, existSection);
    }

    private void validateDistance(final Integer distance, final Section existSection) {
        if (existSection.getDistance().getValue() <= distance) {
            throw new IllegalArgumentException("새롭게 등록하는 구간의 거리는 기존에 존재하는 구간의 거리보다 작아야합니다.");
        }
    }

    private List<Section> divideSectionByAddedStation(final Long lineId, final Long addedId, final Boolean direction, final Integer distance, final Section existSection) {
        sectionDao.deleteById(existSection.getId());

        final Section upSection = createUpSectionByDirection(lineId, addedId, direction, distance, existSection);
        final Section upSavedSection = sectionDao.insert(upSection);
        final Section downSection = createDownSectionByDirection(lineId, addedId, direction, distance, existSection);
        final Section downSavedSection = sectionDao.insert(downSection);
        return List.of(upSavedSection, downSavedSection);
    }

    private Section createUpSectionByDirection(final Long lineId, final Long addedId, final Boolean direction, final Integer distance, final Section existSection) {
        if (Direction.from(direction) == Direction.UP) {
            return Section.builder()
                    .lineId(lineId)
                    .upStation(existSection.getUpStation().getId())
                    .downStation(addedId)
                    .distance(existSection.getDistance().getValue() - distance)
                    .build();
        }
        if (Direction.from(direction) == Direction.DOWN) {
            return Section.builder()
                    .lineId(lineId)
                    .upStation(existSection.getUpStation().getId())
                    .downStation(addedId)
                    .distance(distance)
                    .build();
        }
        throw new IllegalArgumentException("존재하지 않는 방향입니다.");
    }

    private Section createDownSectionByDirection(final Long lineId, final Long addedId, final Boolean direction, final Integer distance, final Section existSection) {
        if (Direction.from(direction) == Direction.UP) {
            return Section.builder()
                    .lineId(lineId)
                    .upStation(addedId)
                    .downStation(existSection.getDownStation().getId())
                    .distance(distance)
                    .build();
        }
        if (Direction.from(direction) == Direction.DOWN) {
            return Section.builder()
                    .lineId(lineId)
                    .upStation(addedId)
                    .downStation(existSection.getDownStation().getId())
                    .distance(existSection.getDistance().getValue() - distance)
                    .build();
        }
        throw new IllegalArgumentException("존재하지 않는 방향입니다.");
    }
}
