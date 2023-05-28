package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.entity.LineEntity;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class DBLineRepository implements LineRepository {

    private final LineDao lineDao;

    public DBLineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Line insert(Line line) {
        LineEntity lineEntity = new LineEntity(null, line.getName());
        LineEntity insertedLineEntity = lineDao.insert(lineEntity);
        return new Line(insertedLineEntity.getId(), insertedLineEntity.getLineName());
    }

    @Override
    public Line findLineById(Long lineId) {
        LineEntity findLineEntity = lineDao.findById(lineId);
        return new Line(findLineEntity.getId(), findLineEntity.getLineName());
    }

    @Override
    public Optional<Line> findByLineName(String lineName) {
        Optional<LineEntity> nullableLineEntity = lineDao.findByLineName(lineName);
        if (nullableLineEntity.isEmpty()) {
            return Optional.empty();
        }
        LineEntity findLineEntity = nullableLineEntity.get();
        return Optional.of(new Line(findLineEntity.getId(), findLineEntity.getLineName()));
    }

    @Override
    public List<Line> findAllLines() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getLineName()))
                .collect(toList());
    }

    @Override
    public void update(Line newLine) {
        LineEntity newLineEntity = new LineEntity(newLine.getId(), newLine.getName());
        lineDao.updateById(newLineEntity);
    }

    @Override
    public void deleteById(Long lineId) {
        lineDao.deleteById(lineId);
    }
}
