package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.Entity.LineEntity;
import subway.Entity.SectionEntity;
import subway.controller.exception.OptionalHasNoLineException;
import subway.controller.exception.OptionalHasNoStationException;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.persistence.NullChecker;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class H2SectionRepository implements SectionRepository {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public H2SectionRepository(StationDao stationDao, LineDao lineDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public void updateAllSectionsInLine(LineEntity lineEntity, List<Section> lineSections) {
        NullChecker.isNull(lineEntity);
        NullChecker.isNull(lineSections);
        sectionDao.deleteAllByLineId(lineEntity.getId());
        List<SectionEntity> updatedSectionEntities = convertNewSectionsToEntities(lineEntity, lineSections);
        sectionDao.insertAll(updatedSectionEntities);
    }

    private List<SectionEntity> convertNewSectionsToEntities(LineEntity lineEntity, List<Section> lineSections) {
        return lineSections.stream()
                .map(section -> new SectionEntity(
                        null,
                        lineEntity.getId(),
                        stationDao.findIdByName(section.getUpward().getName())
                                .orElseThrow(OptionalHasNoStationException::new),
                        stationDao.findIdByName(section.getDownward().getName())
                                .orElseThrow(OptionalHasNoStationException::new),
                        section.getDistance()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<Section> readSectionsByLine(LineEntity lineEntity) {
        NullChecker.isNull(lineEntity);
        return mapToSections(sectionDao.selectSectionsByLineId(lineEntity.getId()));
    }

    @Override
    public List<Section> readAllSections() {
        return mapToSections(sectionDao.selectAllSections());
    }

    private List<Section> mapToSections(List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(entity -> Section.of(
                        lineDao.findById(entity.getLineId())
                                .orElseThrow(OptionalHasNoLineException::new)
                                .mapToLine(),
                        stationDao.findById(entity.getUpwardId())
                                .orElseThrow(OptionalHasNoStationException::new)
                                .mapToStation(),
                        stationDao.findById(entity.getDownwardId())
                                .orElseThrow(OptionalHasNoStationException::new)
                                .mapToStation(),
                        entity.getDistance())
                ).collect(Collectors.toList());
    }
}
