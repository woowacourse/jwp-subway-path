package subway.adapter.out.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.adapter.out.persistence.entity.LineEntity;
import subway.application.port.out.line.LineCommandPort;
import subway.application.port.out.line.LineQueryPort;
import subway.domain.Line;
import subway.adapter.out.persistence.dao.LineDao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class LineJdbcAdapter implements LineCommandPort, LineQueryPort {

    private final LineDao lineDao;

    public LineJdbcAdapter(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Long createLine(final Line line) {
        final LineEntity lineEntity = new LineEntity(line.getName());

        return lineDao.createLine(lineEntity);
    }

    @Override
    public void deleteById(final Long lineIdRequest) {
        lineDao.deleteById(lineIdRequest);
    }

    @Override
    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Line> findById(final Long lineIdRequest) {
        final Optional<LineEntity> lineEntity = lineDao.findById(lineIdRequest);

        return lineEntity.map(line -> new Line(line.getId(), line.getName()));
    }

    @Override
    public Optional<Line> findByName(final Line line) {
        final Set<LineEntity> lines = new HashSet<>(lineDao.findAll());

        return lines.stream()
                .filter(lineEntity -> lineEntity.getName().equals(line.getName()))
                .map(lineEntity -> new Line(lineEntity.getId(), lineEntity.getName()))
                .findFirst();
    }
}
