package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionRepository;
import subway.domain.Section;
import subway.domain.SectionDirection;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionCreateRequest;
import subway.exception.custom.LineDoesNotContainStationException;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Long create(final Long lineId, final SectionCreateRequest request) {
        final Sections sections = Sections.of(sectionRepository.findAllByLineId(lineId));
        final Station baseStation = sectionRepository.insertStationIfNotExist(request.getBaseStation());
        final Station newStation = sectionRepository.insertStationIfNotExist(request.getNewStation());
        final Section newSection = Section.of(baseStation, newStation,
            SectionDirection.get(request.getDirection()), request.getDistance());

        sections.addSection(baseStation, newSection);
        updateSectionsInLine(lineId, sections.getSections());

        return sectionRepository.findIdBy(lineId, newSection);
    }


    private void updateSectionsInLine(final Long lineId, final List<Section> sections) {
        sectionRepository.deleteAllByLineId(lineId);
        sections.forEach(section -> sectionRepository.insert(lineId, section));
    }

    @Transactional
    public void delete(final Long lineId, final String stationName) {
        final Sections sections = Sections.of(sectionRepository.findAllByLineId(lineId));
        final Station findStation = sections.getStations()
            .stream()
            .filter((station -> station.getName().equals(stationName)))
            .findFirst()
            .orElseThrow(() -> new LineDoesNotContainStationException("노선에 역이 존재하지 않습니다. ( " + stationName + " )"));

        sections.removeStation(findStation);
        updateSectionsInLine(lineId, sections.getSections());
    }
}
