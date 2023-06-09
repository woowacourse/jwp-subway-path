package subway.line.db.line;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.line.db.interstation.InterStationEntity;
import subway.line.domain.interstation.InterStation;
import subway.line.domain.line.Line;
import subway.line.domain.line.LineRepository;
import subway.line.domain.line.exception.LineAlreadyRegisteredException;
import subway.line.domain.line.exception.LineNotFoundException;

@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;

    public LineRepositoryImpl(JdbcTemplate jdbcTemplate) {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Override
    public Line save(Line line) {
        try {
            LineEntity lineEntity = lineDao.insert(LineEntity.from(line));
            return lineEntity.toLine();
        } catch (DuplicateKeyException e) {
            throw new LineAlreadyRegisteredException();
        }
    }

    @Override
    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(LineEntity::toLine)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Line> findById(long id) {
        return lineDao.findById(id)
                .map(LineEntity::toLine);
    }

    @Override
    public void update(Line line) {
        Line existLine = findById(line.getId())
                .orElseThrow(LineNotFoundException::new);
        updateInformation(line, existLine);
        addInterStations(line, existLine);
        removeInterStations(line, existLine);
    }

    private void updateInformation(Line line, Line existLine) {
        if (!line.getName().equals(existLine.getName())) {
            lineDao.updateInformation(LineEntity.from(line));
            return;
        }
        if (!line.getColor().equals(existLine.getColor())) {
            lineDao.updateInformation(LineEntity.from(line));
        }
    }

    private void addInterStations(Line line, Line existLine) {
        List<InterStation> interStations = new ArrayList<>(line.getInterStations().getInterStations());
        List<InterStation> existInterStations = new ArrayList<>(existLine.getInterStations().getInterStations());
        interStations.removeAll(existInterStations);
        if (!interStations.isEmpty()) {
            lineDao.insertInterStations(toInterStationEntities(interStations, line.getId()));
        }
    }

    private void removeInterStations(Line line, Line existLine) {
        List<InterStation> interStations = new ArrayList<>(line.getInterStations().getInterStations());
        List<InterStation> existInterStations = new ArrayList<>(existLine.getInterStations().getInterStations());
        existInterStations.removeAll(interStations);
        if (!existInterStations.isEmpty()) {
            lineDao.deleteInterStations(toInterStationEntities(existInterStations, line.getId()));
        }
    }

    private List<InterStationEntity> toInterStationEntities(List<InterStation> interStations,
            long id) {
        return interStations.stream()
                .map(interStation -> InterStationEntity.of(interStation, id))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        lineDao.deleteById(id);
    }
}
