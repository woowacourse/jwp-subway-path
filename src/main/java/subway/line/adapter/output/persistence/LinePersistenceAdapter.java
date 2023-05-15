package subway.line.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subway.line.application.port.output.FindAllLinePort;
import subway.line.application.port.output.FindLineByIdPort;
import subway.line.application.port.output.SaveLinePort;
import subway.line.domain.Line;
import subway.section.adapter.output.persistence.SectionDao;
import subway.section.adapter.output.persistence.SectionEntity;
import subway.section.domain.Section;
import subway.station.adapter.output.persistence.StationDao;
import subway.station.adapter.output.persistence.StationEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class LinePersistenceAdapter implements FindAllLinePort, SaveLinePort, FindLineByIdPort {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    
    @Override
    public Set<Line> findAll() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<SectionEntity> sectionEntities = sectionDao.findAll();
        
        final Map<Long, List<SectionEntity>> lineSectionMatcher = sectionEntities.stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));
        
        return lineEntities.stream()
                .map(lineEntity -> toLine(lineEntity, lineSectionMatcher.getOrDefault(lineEntity.getId(), Collections.emptyList())))
                .collect(Collectors.toSet());
    }
    
    private Line toLine(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        final Set<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> {
                    final StationEntity firstStationEntity = stationDao.findById(sectionEntity.getFirstStationId());
                    final StationEntity secondStationEntity = stationDao.findById(sectionEntity.getSecondStationId());
                    return new Section(firstStationEntity.getName(), secondStationEntity.getName(), sectionEntity.getDistance());
                })
                .collect(Collectors.toSet());
        
        return new Line(lineEntity.getName(), lineEntity.getColor(), sections);
    }
    
    @Override
    public Long save(final Line line) {
        return lineDao.insert(new LineEntity(line.getName(), line.getColor()));
    }
    
    @Override
    public Line findById(final Long id) { // TODO
        final Set<Section> sections = sectionDao.findByLineId(id).stream()
                .map(sectionEntity -> {
                    final StationEntity first = stationDao.findById(sectionEntity.getFirstStationId());
                    final StationEntity second = stationDao.findById(sectionEntity.getSecondStationId());
                    return new Section(first.getName(), second.getName(), sectionEntity.getDistance());
                })
                .collect(Collectors.toSet());
        final LineEntity lineEntity = lineDao.findById(id);
        
        return new Line(lineEntity.getName(), lineEntity.getColor(), sections);
    }
}
