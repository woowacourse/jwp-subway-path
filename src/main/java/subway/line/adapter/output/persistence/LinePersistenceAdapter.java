package subway.line.adapter.output.persistence;

import org.springframework.stereotype.Repository;
import subway.line.application.port.output.DeleteLinePort;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.application.port.output.SaveLinePort;
import subway.line.domain.Line;
import subway.section.adapter.output.persistence.SectionDao;
import subway.section.adapter.output.persistence.SectionEntity;
import subway.section.domain.Section;
import subway.station.adapter.output.persistence.StationDao;
import subway.station.adapter.output.persistence.StationEntity;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LinePersistenceAdapter implements GetAllLinePort, SaveLinePort, GetLineByIdPort, DeleteLinePort {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    
    public LinePersistenceAdapter(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }
    
    @Override
    public Set<Line> getAll() {
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
                    return new Section(
                            firstStationEntity.getName(),
                            secondStationEntity.getName(),
                            sectionEntity.getDistance(),
                            lineEntity.getName()
                    );
                })
                .collect(Collectors.toSet());
        
        return new Line(lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraCharge(), sections);
    }
    
    @Override
    public Long save(final Line line) {
        return lineDao.insert(new LineEntity(line.getName(), line.getColor(), line.getExtraCharge()));
    }
    
    @Override
    public Line getLineById(final Long id) {
        validateNotExistLine(id);
        
        final LineEntity lineEntity = lineDao.findById(id);
        final Set<Section> sections = sectionDao.findByLineId(id).stream()
                .map(sectionEntity -> {
                    final StationEntity first = stationDao.findById(sectionEntity.getFirstStationId());
                    final StationEntity second = stationDao.findById(sectionEntity.getSecondStationId());
                    return new Section(
                            first.getName(),
                            second.getName(),
                            sectionEntity.getDistance(),
                            lineEntity.getName()
                    );
                })
                .collect(Collectors.toSet());
        
        return new Line(lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraCharge(), sections);
    }
    
    private void validateNotExistLine(final Long id) {
        if (isNotExistLine(id)) {
            throw new IllegalArgumentException("존재하지 않는 노선을 지정하셨습니다.");
        }
    }
    
    private boolean isNotExistLine(final Long id) {
        return lineDao.findAll().stream()
                .map(LineEntity::getId)
                .noneMatch(lineId -> Objects.equals(lineId, id));
    }
    
    @Override
    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
