package subway.section.service;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.dao.SectionDao;
import subway.section.domain.Direction;
import subway.section.domain.Section;

@AllArgsConstructor
@Service
public class SectionService {

    private SectionDao sectionDao;

    @Transactional
    public List<Section> createSection(
            final Long lineId,
            final Long baseId,
            final Long addedId,
            final Boolean direction,
            final Integer distance
    ) {
        final Optional<Section> section = sectionDao.findNeighborSection(lineId, baseId, Direction.from(direction));

        if (section.isEmpty()) {
            return createSectionWhenNoNeighbor(lineId, baseId, addedId, direction, distance);
        }

        final Section existSection = section.get();

        if (existSection.getDistance() <= distance) {
            throw new IllegalArgumentException("새롭게 등록하는 구간의 거리는 기존에 존재하는 구간의 거리보다 작아야합니다.");
        }

        return divideSectionByAddedStation(lineId, addedId, direction, distance, existSection);
    }

    private List<Section> createSectionWhenNoNeighbor(final Long lineId, final Long baseId, final Long addedId, final Boolean direction, final Integer distance) {
        if (Direction.from(direction) == Direction.UP) {
            final Section newSection = new Section(lineId, addedId, baseId, distance);
            final Section savedSection = sectionDao.insert(newSection);
            return List.of(savedSection);
        }
        final Section newSection = new Section(lineId, baseId, addedId, distance);
        final Section savedSection = sectionDao.insert(newSection);
        return List.of(savedSection);
    }

    private List<Section> divideSectionByAddedStation(final Long lineId, final Long addedId, final Boolean direction, final Integer distance, final Section existSection) {
        sectionDao.deleteById(existSection.getId());

        if (Direction.from(direction) == Direction.UP) {
            final Section upSection = new Section(lineId, existSection.getUpStationId(), addedId, existSection.getDistance() - distance);
            final Section upSavedSection = sectionDao.insert(upSection);
            final Section downSection = new Section(lineId, addedId, existSection.getDownStationId(), distance);
            final Section downSavedSection = sectionDao.insert(downSection);
            return List.of(upSavedSection, downSavedSection);
        }
        final Section upSection = new Section(lineId, existSection.getUpStationId(), addedId, distance);
        final Section upSavedSection = sectionDao.insert(upSection);
        final Section downSection = new Section(lineId, addedId, existSection.getDownStationId(), existSection.getDistance() - distance);
        final Section downSavedSection = sectionDao.insert(downSection);
        return List.of(upSavedSection, downSavedSection);
    }

    public void deleteSection(final Long lineId, final Long stationId) {
        final Optional<Section> upSection = sectionDao.findNeighborUpSection(lineId, stationId);
        final Optional<Section> downSection = sectionDao.findNeighborDownSection(lineId, stationId);

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

        final Section existUpSection = upSection.get();
        final Section existDownSection = downSection.get();

        sectionDao.deleteById(existUpSection.getId());
        sectionDao.deleteById(existDownSection.getId());

        final Section section = new Section(
                lineId,
                existUpSection.getUpStationId(),
                existDownSection.getDownStationId(),
                existUpSection.getDistance() + existDownSection.getDistance()
        );

        sectionDao.insert(section);
    }
}
