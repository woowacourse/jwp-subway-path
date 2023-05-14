package subway.repository;

import org.springframework.stereotype.Repository;
import subway.Entity.LineEntity;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Optional<Line> findById(long id) {
        Optional<LineEntity> optional = lineDao.findById(id);
        if(optional.isEmpty()){
            return Optional.empty();
        }
        LineEntity lineEntity = optional.get();
        List<Section> sections = sectionDao.selectSectionsByLineId(id);
        return Optional.of(Line.of(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sections));
    }

    public List<Line> findAll() {
        List<Line> lines = new ArrayList<>();
        List<LineEntity> lineEntities = lineDao.findAll();
        for(LineEntity lineEntity: lineEntities){
            List<Section> lineSections = sectionDao.selectSectionsByLineId(lineEntity.getId());
            lines.add(Line.of(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineSections));
        }
        return lines;
    }

    public Line insert(final Line line){
        LineEntity lineEntity = lineDao.insert(Line.toEntity(line));
        long lineId = lineEntity.getId();
        for(Section section: line.getSections()){
            sectionDao.insert(section, lineId);
        }
        return Line.of(lineId, line.getName(), line.getColor(), line.getSections());
    }

    public void update(final Line line) {
        lineDao.update(Line.toEntity(line));
    }

    public void deleteById(final long id) {
        sectionDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }
}
