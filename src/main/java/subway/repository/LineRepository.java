package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.exception.line.LineAlreadyExistException;
import subway.exception.line.LineDoesNotExistException;

@Repository
public class LineRepository {

    public static final int INITIAL_SECTION = 0;

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line getLine(Long lineId) {
        return assembleLine(lineId);
    }

    public List<Line> getAllLines() {
        return lineDao.findAll()
                .stream()
                .map(line -> assembleLine(line.getId()))
                .collect(Collectors.toList());
    }

    public Line insertNewLine(Line createdLine) {
        Long lineId = lineDao.insert(createdLine);
        sectionDao.insert(lineId, createdLine.getSections().get(INITIAL_SECTION));
        return assembleLine(lineId);
    }

    public Line updateLine(Line updatedLine) {
        sectionDao.deleteAllByLineId(updatedLine.getId());
        sectionDao.insertAllByLineId(updatedLine.getId(), updatedLine.getSections());
        return assembleLine(updatedLine.getId());
    }

    public void checkLineIsExist(String lineName) {
        lineDao.findByName(lineName)
                .ifPresent(line -> {
                    throw new LineAlreadyExistException();
                });
    }

    public Line assembleLine(Long lineId) {
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new LineDoesNotExistException());

        List<Section> sections = sectionDao.findSectionsByLineId(line.getId());
        return new Line(line.getId(), line.getName(), line.getExtraCharge(), sections);
    }
}
