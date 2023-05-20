package subway.line.application;

import org.springframework.stereotype.Repository;
import subway.line.Line;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.station.Station;
import subway.line.infrastructure.LineDao;

import java.util.List;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public LineRepository(LineDao lineDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public Line insert(String name, String color) {
        return lineDao.insert(name, color);
    }

    public List<Line> findAll() {
        final var lines = lineDao.findAll();
        for (Line line : lines) {
            final var sections = sectionRepository.findAllByLineId(line.getId());
            line.addSections(sections);
        }
        return lines;
    }

    public Line findById(Long id) {
        final var line = lineDao.findById(id);
        final var sections = sectionRepository.findAllByLineId(line.getId());
        line.addSections(sections);
        return line;
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
