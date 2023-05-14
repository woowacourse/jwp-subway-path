package subway.application;

import org.springframework.stereotype.Service;
import subway.application.sectionreader.FirstSaveCase;
import subway.application.sectionreader.SectionReader;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SectionSorter;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<AddStationResponse> addStationByLineId(final Long id, AddStationRequest addStationRequest) throws IllegalAccessException {
        final List<Section> allSections = sectionDao.findSectionsByLineId(id);
        SectionReader sectionReader = new FirstSaveCase(addStationRequest, sectionDao);

        return sectionReader.read(id, allSections);
    }

    public Map<Line, List<Section>> findSections() {
        final SectionSorter sectionSorter = SectionSorter.getInstance();

        return sectionDao.findSections().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> sectionSorter.sortSections(entry.getValue())));
    }

    public List<Section> findSectionsById(Long id) {
        final List<Section> sections = sectionDao.findSectionsByLineId(id);
        final SectionSorter sectionSorter = SectionSorter.getInstance();

        return sectionSorter.sortSections(sections);
    }

    public void deleteSectionByLineIdAndSectionId(Long lineId, Long sectionId) {
        final List<Section> sections = sectionDao.findSectionByLineIdAndStationId(lineId, sectionId);
        final SectionSorter sectionSorter = SectionSorter.getInstance();
        final List<Section> sortedSections = sectionSorter.sortSections(sections);
        if (sections.size() == 0) {
            throw new IllegalArgumentException("해당하는 역이 없습니다.");
        }
        sectionDao.deleteSection(sections.get(0).getId());
        if (sections.size() == 2) {
            sectionDao.deleteSection(sections.get(1).getId());
            sectionDao.saveSection(lineId,
                    sections.get(0).getDistance().getDistance() + sections.get(1).getDistance().getDistance(),
                    sortedSections.get(0).getDeparture().getName(), sortedSections.get(1).getArrival().getName());
        }
    }
}
