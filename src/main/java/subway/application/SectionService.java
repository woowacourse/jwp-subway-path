package subway.application;

import org.springframework.stereotype.Service;
import subway.application.reader.CaseDto;
import subway.application.reader.FirstFindCase;
import subway.application.reader.Reader;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.SectionSorter;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static subway.application.reader.CaseTypeSetter.setCase;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<Section> addStationByLineId(final Long id, final String departure, final String arrival, final int distance) throws IllegalAccessException {
        final List<Section> allSections = sectionDao.findSectionsByLineId(id);
        CaseDto caseDto = new CaseDto.Builder()
                .lineId(id)
                .departure(departure)
                .arrival(arrival)
                .distance(distance)
                .build();
        caseDto = setCase(caseDto, allSections);
        Reader reader = new FirstFindCase(sectionDao);
        return reader.read(caseDto);
    }

    public Map<Line, List<Section>> findSections() {

        return sectionDao.findSections().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> SectionSorter.sorting(entry.getValue())));
    }

    public List<Section> findSectionsById(Long id) {
        final List<Section> sections = sectionDao.findSectionsByLineId(id);

        return SectionSorter.sorting(sections);
    }

    public void deleteSectionByLineIdAndSectionId(Long lineId, Long sectionId) {
        final List<Section> sections = sectionDao.findSectionByLineIdAndStationId(lineId, sectionId);

        final List<Section> sortedSections = SectionSorter.sorting(sections);
        if (sections.size() == 0) {
            throw new IllegalArgumentException("해당하는 역이 없습니다.");
        }
        sectionDao.deleteSection(sections.get(0).getId());
        if (sections.size() == 2) {
            sectionDao.deleteSection(sections.get(1).getId());
            sectionDao.saveSection(lineId,
                    sections.get(0).getDistanceValue()+ sections.get(1).getDistanceValue(),
                    sortedSections.get(0).getDepartureValue(), sortedSections.get(1).getArrivalValue());
        }
    }
}
