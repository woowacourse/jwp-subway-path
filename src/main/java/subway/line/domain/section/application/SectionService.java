package subway.line.domain.section.application;

import org.springframework.stereotype.Service;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<Section> findAllByLineId(Long id) {
        return sectionRepository.findAllByLineId(id);
    }

    public Section insert(Long lineId, Station previousStation, Station nextStation, Distance distance) {
        return sectionRepository.insert(lineId, previousStation, nextStation, distance);
    }

    public Section update(Section section) {
        sectionRepository.update(section);
        return section;
    }

    public void clearStations(Long lineId) {
        sectionRepository.clearStations(lineId);
    }

    public void delete(Section section) {
        sectionRepository.delete(section);
    }

    public long findLineIdBySectionHavingStations(Station stationA, Station stationB) {
        return sectionRepository.findLineIdBySectionHavingStations(stationA, stationB);
    }
}
