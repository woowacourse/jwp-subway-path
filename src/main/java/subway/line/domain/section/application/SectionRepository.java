package subway.line.domain.section.application;

import org.springframework.stereotype.Repository;
import subway.line.Line;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.infrastructure.SectionDao;
import subway.line.domain.station.Station;

import java.util.List;
import java.util.Optional;

@Repository
public class SectionRepository {
    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }


    public Optional<Section> findByPreviousStation(Station previousStation, Line line) {
        return sectionDao.findByPreviousStation(previousStation, line);
    }

    public Optional<Section> findByNextStation(Station nextStation, Line line) {
        return sectionDao.findByNextStation(nextStation, line);
    }

    public List<Section> findAllByLine(Line line) {
        return sectionDao.findAllByLine(line);
    }

    public Section insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        return sectionDao.insert(line, previousStation, nextStation, distance);
    }

    public void update(Section section) {
        sectionDao.update(section);
    }

    public int countStations(Line line) {
        return sectionDao.countStations(line);
    }

    public void clearStations(Line line) {
        sectionDao.clearStations(line);
    }

    public void deleteStation(Station station, Line line) {
        sectionDao.deleteStation(station, line);
    }
}
