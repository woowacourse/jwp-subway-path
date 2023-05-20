package subway.line.domain.section.application;

import org.springframework.stereotype.Repository;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.infrastructure.SectionDao;
import subway.line.domain.station.Station;

import java.util.List;

@Repository
public class SectionRepository {
    private final SectionDao sectionDao;

    public SectionRepository(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> findAllByLineId(Long id) {
        return sectionDao.findAllByLineId(id);
    }

    public Section insert(Long lineId, Station previousStation, Station nextStation, Distance distance) {
        return sectionDao.insert(lineId, previousStation, nextStation, distance);
    }

    public Section update(Section section) {
        sectionDao.update(section);
        return section;
    }

    public void clearStations(Long lineId) {
        sectionDao.clearStations(lineId);
    }

    public void delete(Section section) {
        sectionDao.delete(section);
    }
}
