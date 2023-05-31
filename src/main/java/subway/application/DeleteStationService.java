package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.exception.StationNotFoundException;

import java.util.List;

@Service
@Transactional
public class DeleteStationService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public DeleteStationService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public Long deleteStationById(Long id) {
        Station stationToDelete = stationRepository.findStationById(id);
        Line targetLine = stationToDelete.getLine();
        Sections sections = new Sections(sectionRepository.findSectionsByLineId(targetLine.getId()));
        List<Section> sectionsToCombine = sections.findSectionsContainStation(stationToDelete);

        removeStationIfExist(stationToDelete, sectionsToCombine);

        return updateLineAndSectionAfterRemoveStation(sections, sectionsToCombine);
    }

    private Long updateLineAndSectionAfterRemoveStation(Sections sections, List<Section> sectionsToCombine) {
        Long targetLineId = sections.getLineId();
        if (sections.isOnlyTwoStationsExist()) {
            lineRepository.deleteById(targetLineId);
            return targetLineId;
        }
        if (wasDeleteEndStation(sectionsToCombine)) {
            return targetLineId;
        }
        return combineTwoSectionsAndUpdate(sectionsToCombine);
    }

    private Long combineTwoSectionsAndUpdate(List<Section> sectionsToCombine) {
        Section combinedSection = combineSections(sectionsToCombine);
        Section insertedSection = sectionRepository.insert(combinedSection);
        return insertedSection.getLineId();
    }

    private Section combineSections(List<Section> sectionsToCombine) {
        Section section1 = sectionsToCombine.get(0);
        Section section2 = sectionsToCombine.get(1);
        return section1.combine(section2);
    }

    private boolean wasDeleteEndStation(List<Section> sectionsToCombine) {
        return sectionsToCombine.size() == 1;
    }

    private void removeStationIfExist(Station stationToDelete, List<Section> sectionsToCombine) {
        if (sectionsToCombine.isEmpty()) {
            throw new StationNotFoundException();
        }
        stationRepository.remove(stationToDelete);
    }
}
