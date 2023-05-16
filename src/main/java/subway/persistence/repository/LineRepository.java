package subway.persistence.repository;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.section.Section;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;

@Repository
public class LineRepository {

    private static final Comparator<Section> SECTION_COMPARATOR = (section1, section2) -> {
        if (section1.isEqualNextStation(section2.getPrevStation())) {
            return -1;
        }
        if (section1.isEqualPrevStation(section2.getNextStation())) {
            return 1;
        }
        return 0;
    };

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
        Line line = lineDao.findById(id);
        final List<Section> sections = sectionDao.findByLineId(id);
        sections.sort(SECTION_COMPARATOR);
        for (final Section section : sections) {
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

    public void updateSections(final List<Section> deleteSections, final List<Section> insertSections,
                               final Long lineId) {
        sectionDao.delete(deleteSections);
        sectionDao.insert(lineId, insertSections);
    }
}
