package subway.persistence.repository;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.section.Sections;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
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
        final Line line = lineDao.findById(id);
        final Sections sections = Sections.initSections(sectionDao.findByLineId(id));
        return new Line(line.getId(), line.getName(), sections);
    }

    public void updateName(final Long id, final LineName lineName) {
        lineDao.updateName(id, lineName);
    }

    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }

    public void updateLine(final Line originLine, final Line updatedLine) {
        final Sections deleteSections = originLine.getSections().getDifferenceOfSet(updatedLine.getSections());
        final Sections insertSections = updatedLine.getSections().getDifferenceOfSet(originLine.getSections());

        sectionDao.delete(deleteSections.getSections());
        sectionDao.insert(originLine.getId(), insertSections.getSections());
    }
}
