package subway.line.application;

import org.springframework.stereotype.Repository;
import subway.line.Line;
import subway.line.domain.station.Station;
import subway.line.infrastructure.LineDao;

import java.util.List;

@Repository
public class LineRepository {
    private final LineDao lineDao;

    public LineRepository(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Line insert(String name, String color) {
        return lineDao.insert(name, color);
    }

    public List<Line> findAll() {
        return lineDao.findAll();
    }

    public Line findById(Long id) {
        return lineDao.findById(id);
    }

    public void update(Line line) {
        lineDao.update(line);
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }

    public void updateHeadStation(Line line, Station station) {
        lineDao.updateHeadStation(line, station);
    }
}
