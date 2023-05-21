package subway.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.SortedStations;
import subway.domain.Station;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;
import subway.exception.SectionRemovalException;
import subway.persistence.repository.SectionRepository;

import java.util.List;

@Transactional
@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Long addStation(Long lineId, LineStationRequest lineStationRequest) {
        Section section = sectionRepository.toSection(lineId, lineStationRequest);
        Sections currentLineSections = sectionRepository.getCurrentLineSections(lineId);
        Section modified = currentLineSections.add(section);

        if (modified != null) {
            sectionRepository.updateSectionAfterAddition(lineId, modified, lineStationRequest);
        }
        return sectionRepository.saveSection(lineId, lineStationRequest);
    }


    public void removeStation(Long lineId, Long stationId) {
        Sections currentLineSections = sectionRepository.getCurrentLineSections(lineId);
        Station station = sectionRepository.findStationById(stationId);

        if (!currentLineSections.isExistStation(station)) {
            throw new SectionRemovalException("노선에 없는 역은 삭제할 수 없습니다");
        }

        Section modified = currentLineSections.remove(station);
        if (modified != null) {
            sectionRepository.updateSectionAfterRemoval(lineId, modified);
        }

        sectionRepository.removeSection(stationId);
    }

    public LineStationResponse findByLineId(Long lineId) {
        Sections currentSections = sectionRepository.getCurrentLineSections(lineId);

        List<Station> stations = SortedStations.from(currentSections).getStations();
        return new LineStationResponse(lineId, stations);
    }
}
