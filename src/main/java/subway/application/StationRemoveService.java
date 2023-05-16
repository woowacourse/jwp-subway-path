package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.section.*;
import subway.domain.station.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
public class StationRemoveService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public StationRemoveService(final StationRepository stationRepository, final SectionRepository sectionRepository,
                                final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    public void removeStationById(Long stationId) {
        Station findStation = stationRepository.findStationById(stationId);
        System.out.println("findStation = " + findStation);
        Long lineId = findStation.getLine().getId();
        Sections sections = new Sections(sectionRepository.findAllSectionByLineId(lineId));

        if (sections.hasSectionOnlyOne()) {
            lineRepository.removeLineById(lineId);
            return;
        }
        removeStationByCase(findStation, sections);
    }

    private void removeStationByCase(Station findStation, Sections sections) {
        SectionCase sectionCase = sections.determineSectionCaseByStationId(findStation.getId());
        System.out.println("findStation Id = " + findStation.getId());
        if (sectionCase.equals(SectionCase.END_SECTION)) {
            stationRepository.removeStationById(findStation.getId());
        }

        if (sectionCase.equals(SectionCase.MIDDLE_SECTION)) {
            stationRepository.removeStationById(findStation.getId());
            saveNewSection(findStation, sections);
        }
    }

    private void saveNewSection(Station findStation, Sections sections) {
        Section upSection = sections.findSectionByDownStation(findStation);
        Section downSection = sections.findSectionByUpStation(findStation);

        int upDistance = upSection.getDistance().getDistance();
        int downDistance = downSection.getDistance().getDistance();
        Distance newDistance = new Distance(upDistance + downDistance);

        Station newUpStation = upSection.getUpStation();
        Station newDownStation = downSection.getDownStation();

        Line line = findStation.getLine();
        Section sectionToSave = NewSectionMaker.makeSectionToSave(line, newUpStation, newDownStation, newDistance);
        sectionRepository.saveSection(sectionToSave);
    }
}
