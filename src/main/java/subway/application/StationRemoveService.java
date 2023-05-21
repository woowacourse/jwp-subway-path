package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.general.GeneralSection;
import subway.domain.section.general.GeneralSectionCase;
import subway.domain.section.general.GeneralSections;
import subway.domain.section.general.NewGeneralSectionMaker;
import subway.domain.station.Station;
import subway.repository.GeneralSectionRepository;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class StationRemoveService {

    private final StationRepository stationRepository;
    private final GeneralSectionRepository generalSectionRepository;
    private final LineRepository lineRepository;

    public StationRemoveService(final StationRepository stationRepository, final GeneralSectionRepository generalSectionRepository,
                                final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.generalSectionRepository = generalSectionRepository;
        this.lineRepository = lineRepository;
    }

    public void removeStationById(Long stationId) {
        Station findStation = stationRepository.findStationById(stationId);
        Long lineId = findStation.getLine().getId();
        GeneralSections generalSections = new GeneralSections(generalSectionRepository.findAllSectionByLineId(lineId));

        if (generalSections.hasSectionOnlyOne()) {
            lineRepository.removeLineById(lineId);
            return;
        }
        removeStationByCase(findStation, generalSections);
    }

    private void removeStationByCase(Station findStation, GeneralSections generalSections) {
        GeneralSectionCase generalSectionCase = generalSections.determineSectionCaseByStationId(findStation.getId());
        if (generalSectionCase.equals(GeneralSectionCase.END_SECTION)) {
            stationRepository.removeStationById(findStation.getId());
        }

        if (generalSectionCase.equals(GeneralSectionCase.MIDDLE_SECTION)) {
            stationRepository.removeStationById(findStation.getId());
            saveNewSection(findStation, generalSections);
        }
    }

    private void saveNewSection(Station findStation, GeneralSections generalSections) {
        GeneralSection upSection = generalSections.findSectionByDownStation(findStation);
        GeneralSection downSection = generalSections.findSectionByUpStation(findStation);

        int upDistance = upSection.getDistance();
        int downDistance = downSection.getDistance();
        Distance newDistance = new Distance(upDistance + downDistance);

        Station newUpStation = upSection.getUpStation();
        Station newDownStation = downSection.getDownStation();

        Line line = findStation.getLine();
        GeneralSection sectionToSave = NewGeneralSectionMaker.makeSectionToSave(newUpStation, newDownStation, newDistance);
        generalSectionRepository.saveSection(sectionToSave);
    }
}
