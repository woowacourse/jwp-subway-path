package subway.line.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subway.line.application.port.output.LineRepository;
import subway.line.domain.Line;
import subway.section.adapter.output.persistence.SectionDao;
import subway.section.adapter.output.persistence.SectionEntity;
import subway.section.domain.Section;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class LineRepositoryImpl implements LineRepository {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    
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
                .map(sectionEntity -> new Section(sectionEntity.getFirstStation(), sectionEntity.getSecondStation(), sectionEntity.getDistance()))
                .collect(Collectors.toUnmodifiableSet());
        
        return new Line(lineEntity.getName(), lineEntity.getColor(), sections);
    }
    
    @Override
    public Long save(final Line line) {
        return lineDao.insert(new LineEntity(line.getName(), line.getColor()));
    }
}
