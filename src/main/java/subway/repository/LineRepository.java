package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.dao.LineDao;
import subway.domain.line.Section;
import subway.domain.line.Line;

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
                    throw new IllegalArgumentException("이미 존재하는 노선입니다.");
                });
    }

    public Line assembleLine(Long lineId) {
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));

        List<Section> sections = sectionDao.findSectionsByLineId(line.getId());
        return new Line(line.getId(), line.getName(), line.getExtraCharge(), sections);
    }
}
