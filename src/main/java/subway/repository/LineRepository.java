package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.exception.LineNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line saveWithSections(final Line line) {
        final Line insertedLine = lineDao.insert(line.getName());
        sectionDao.insertAllByLineId(insertedLine.getId(), line.sections());

        return findLineWithSectionsByLineId(insertedLine.getId()).get();
    }

    public Optional<Line> findLineByName(final Line line) {

        return lineDao.findByName(line.getName());
    }

    public Optional<Line> findLineWithSectionsByLineId(final Long lineId) {
        final Line findLine = lineDao.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
        final Sections sections = new Sections(sectionDao.findAllByLineId(findLine.getId()));

        return Optional.of(new Line(findLine.getId(), findLine.getName(), sections));
    }

    public Optional<Line> findById(final Long lineId) {

        return lineDao.findById(lineId);
    }

    public List<Line> findAllWithSections() {
        final List<Line> allLine = lineDao.findAll();

        return allLine.stream()
                .map(line -> new Line(
                        line.getId(), line.getName(),
                        new Sections(new LinkedList<>(sectionDao.findAllByLineId(line.getId())))))
                .collect(Collectors.toList());
    }
}
