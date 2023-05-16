package subway.persistence.repository;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Section;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Line insert(final LineName lineName) {
        return lineDao.insert(lineName);
    }

    public List<Line> findAll() {
        return lineDao.findAll().stream()
                .map(line -> findById(line.getId()))
                .collect(toUnmodifiableList());
    }

    public Line findById(final Long id) {
        Line line = lineDao.findById(id);
        for (final Section section : sectionDao.findByLineId(id)) {
            line = line.addSection(section);
        }
        return line;
    }

    public void updateName(final Long id, final LineName lineName) {
        lineDao.updateName(id, lineName);
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
