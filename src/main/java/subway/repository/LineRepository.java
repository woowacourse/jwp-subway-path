package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.repository.dao.LineDao;
import subway.repository.dao.SectionDao;

@Repository
public class LineRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public LineRepository(SectionDao sectionDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public Line save(Line line) {
        Line saveLine = lineDao.insert(line);
        List<Section> saveSections = line.getSections().stream()
                .map(section -> sectionDao.insert(section, saveLine.getId()))
                .collect(Collectors.toList());
        saveLine.addSections(saveSections);
        return saveLine;
    }
}
