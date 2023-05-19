package subway.section.adapter.output.persistence;

import org.springframework.stereotype.Repository;
import subway.line.adapter.output.persistence.LineDao;
import subway.line.adapter.output.persistence.LineEntity;
import subway.line.domain.Line;
import subway.section.application.port.output.*;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.adapter.output.persistence.StationDao;
import subway.station.adapter.output.persistence.StationEntity;

import java.util.Set;

@Repository
public class SectionPersistenceAdapter
        implements SaveSectionPort, SaveAllSectionPort, DeleteSectionByLineIdPort, DeleteSectionByLinesPort, SaveAllSectionsOfLinesPort {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    
    public SectionPersistenceAdapter(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }
    
    @Override
    public void saveAll(final Set<Section> sections, final Long lineId) {
        for (final Section section : sections) {
            save(section, lineId);
        }
    }
    
    @Override
    public Long save(final Section section, final Long lineId) {
        final StationEntity leftStationEntity = stationDao.findByName(section.getLeft());
        final StationEntity rightStationEntity = stationDao.findByName(section.getRight());
        
        final SectionEntity sectionEntity = new SectionEntity(
                leftStationEntity.getId(),
                rightStationEntity.getId(),
                section.getDistance().getDistance(),
                lineId
        );
        return sectionDao.insert(sectionEntity);
    }
    
    @Override
    public void deleteSectionByLineId(final Long lineId) {
        sectionDao.deleteByLineId(lineId);
    }
    
    @Override
    public void deleteSectionByLines(final Set<Line> modifiedLines) {
        modifiedLines.stream()
                .map(Line::getName)
                .map(lineDao::findByName)
                .map(LineEntity::getId)
                .forEach(sectionDao::deleteByLineId);
    }
    
    @Override
    public void saveAllSectionsOfLines(final Set<Line> modifiedLines) {
        for (final Line line : modifiedLines) {
            final LineEntity lineEntity = lineDao.findByName(line.getName());
            final Sections sections = line.getSections();
            saveAll(sections.getSections(), lineEntity.getId());
        }
    }
}
