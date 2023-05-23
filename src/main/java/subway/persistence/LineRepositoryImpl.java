package subway.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.exception.BusinessException;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.LineSectionStationJoinDto;
import subway.persistence.entity.SectionEntity;

@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final LineConverter lineConverter = new LineConverter();

    public LineRepositoryImpl(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public Line save(final Line line) {
        final LineEntity lineEntity = LineEntity.from(line);
        final LineEntity savedLineEntity = lineDao.insert(lineEntity);
        return new Line(savedLineEntity.getId(), line.getName(), line.getColor(), lineEntity.getCharge(),
            line.getSections());
    }

    @Override
    public List<Line> findAll() {
        final List<LineSectionStationJoinDto> joinDtos = lineDao.findAll();
        if (joinDtos.isEmpty()) {
            final List<LineEntity> lineEntities = lineDao.findOnlyLines();
            return lineEntities.stream()
                .map(this::makeLine)
                .collect(Collectors.toList());
        }
        return lineConverter.convertToLines(joinDtos);
    }

    private Line makeLine(final LineEntity lineEntity) {
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineEntity.getCharge(),
            new Sections(new ArrayList<>()));
    }

    @Override
    public Optional<Line> findById(final Long id) {
        final List<LineSectionStationJoinDto> joinDtos = lineDao.findById(id);
        if (joinDtos.isEmpty()) {
            final LineEntity lineEntity = lineDao.findOnlyLineById(id);
            if (lineEntity == null) {
                return Optional.empty();
            }
            final Line line = makeLine(lineEntity);
            return Optional.of(line);
        }
        final Line line = lineConverter.convertToLine(joinDtos);
        return Optional.of(line);
    }

    @Override
    public Optional<Line> findByName(final String name) {
        final List<LineSectionStationJoinDto> joinDtos = lineDao.findByName(name);
        if (joinDtos.isEmpty()) {
            final LineEntity lineEntity = lineDao.findOnlyLineByName(name);
            if (lineEntity == null) {
                return Optional.empty();
            }
            final Line line = makeLine(lineEntity);
            return Optional.of(line);
        }
        final Line line = lineConverter.convertToLine(joinDtos);
        return Optional.of(line);
    }

    @Override
    public void update(final Line line) {
        if (line.getSections().isEmpty()) {
            lineDao.update(new LineEntity(line.getId(), line.getName(), line.getColor(), line.getCharge()));
            return;
        }
        final LineEntity lineEntity = LineEntity.from(line);
        sectionDao.deleteAllByLineId(line.getId());
        lineDao.delete(lineEntity);
        final LineEntity savedLineEntity = lineDao.insert(lineEntity);
        final Sections sections = line.getSections();
        for (int index = 0; index < sections.size(); index++) {
            final Section section = sections.findSection(index)
                .orElseThrow(() -> new BusinessException("해당 구간을 찾을 수 없습니다."));
            final SectionEntity sectionEntity = SectionEntity.from(section);
            sectionDao.insert(sectionEntity, savedLineEntity.getId());
        }
    }

    @Override
    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
