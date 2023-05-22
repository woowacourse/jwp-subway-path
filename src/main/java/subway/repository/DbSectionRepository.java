package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DbSectionRepository implements SectionRepository {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public DbSectionRepository(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Section save(final Long lineId, final Section section) {
        final SectionEntity sectionEntity = new SectionEntity(
                lineId,
                section.getUpStation().getId(),
                section.getDownStation().getId(),
                section.getDistance());

        final Long id = sectionDao.insert(sectionEntity).getId();
        return new Section(id, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    @Override
    public void delete(final Long lineId, final Section section) {
        sectionDao.delete(lineId, section.getUpStation().getId(), section.getDownStation().getId());
    }

    @Override
    public void deleteSection(final Long lineId, final Long upStationId, final Long downStationId) {
        sectionDao.delete(lineId, upStationId, downStationId);
    }

    @Override
    public List<Section> findAllSectionOf(final Line line) {
        final List<SectionEntity> sectionEntities = sectionDao.findBy(line.getId());
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        Station.from(stationDao.findBy(sectionEntity.getUpStationId()).get()),
                        Station.from(stationDao.findBy(sectionEntity.getDownStationId()).get()),
                        sectionEntity.getDistance()))
                .collect(Collectors.toList());
    }

    @Override
    public Long findStationIdBefore(final Long lineId, final Long stationId) {
        return sectionDao.findStationIdBefore(lineId, stationId);
    }

    @Override
    public Long findStationIdAfter(final Long lineId, final Long stationId) {
        return sectionDao.findStationIdAfter(lineId, stationId);
    }

    @Override
    public void deleteSectionsOf(final Line line) {
        sectionDao.deleteSectionsOf(line.getId());
    }

    @Override
    public void saveAll(final Line line, final List<Section> sections) {
        for (final Section section : sections) {
            save(line.getId(), section);
        }
    }
}
