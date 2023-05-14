package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.repository.dao.LineDao;
import subway.repository.dao.LineSectionStationJoinDto;
import subway.repository.dao.SectionDao;
import subway.repository.dao.StationDao;

@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepositoryImpl(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Override
    public Line save(final Line line) {
        return lineDao.insert(line);
    }

    @Override
    public List<Line> findAll() {
        return null;
    }

    @Override
    public Line findById(final Long id) {
        return lineDao.findById(id);
    }

    @Override
    public void update(final Line line) {
        sectionDao.deleteAllByLineId(line.getId());
        lineDao.delete(line);
        lineDao.insert(line);
        final Sections sections = line.getSections();
        for (int index = 0; index < sections.size(); index++) {
            final Section section = sections.findSection(index);
            sectionDao.insert(section, line.getId());
        }
    }

    @Override
    public void deleteById(final Long id) {

    }

    @Override
    public Optional<Line> findByName(final String name) {
        final List<LineSectionStationJoinDto> joinDto = lineDao.findByName(name);
        final Line line = new LineConverter().convertToLine(joinDto);
        if (line == null) {
            return Optional.empty();
        }
        return Optional.of(line);
    }
}
