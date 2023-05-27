package subway.application;

import org.springframework.stereotype.Service;
import subway.application.repository.SectionRepository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionRequest;

import java.util.List;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        final Sections nowSections = sectionRepository.findAllSectionByLineId(lineId);
        final Line line = sectionRepository.findLineById(nowSections, lineId);

        final Section newSection = new Section(new Station(sectionRequest.getUpStation()), new Station(sectionRequest.getDownStation()), sectionRequest.getDistance());
        final Sections newSections = line.addSection(newSection).getSections();

        final List<Section> removedSections = nowSections.findRemovedSection(newSections);
        final List<Section> addedSections = newSections.findAddedSection(nowSections);

        updateSections(lineId, removedSections, addedSections);
    }

    public void deleteStationByName(Long lineId, String name) {
        final Sections nowSections = sectionRepository.findAllSectionByLineId(lineId);
        final Sections newSections = nowSections.buildNewSectionsDeleted(new Station(name));

        final List<Section> removedSections = nowSections.findRemovedSection(newSections);
        final List<Section> addedSections = newSections.findAddedSection(nowSections);

        updateSections(lineId, removedSections, addedSections);
    }

    private void updateSections(final Long lineId, final List<Section> removedSections, final List<Section> addedSections) {
        if (removedSections.size() > 0) {
            sectionRepository.deleteSections(lineId, removedSections);
        }
        sectionRepository.saveAllSection(addedSections, lineId);
    }
}