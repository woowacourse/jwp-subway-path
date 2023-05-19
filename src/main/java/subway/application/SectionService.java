package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.exception.AddSectionException;
import subway.application.reader.CaseDto;
import subway.application.reader.InitializationReader;
import subway.application.reader.Reader;
import subway.dao.SectionDao;
import subway.domain.SectionSorter;
import subway.domain.vo.Line;
import subway.domain.vo.RequestInclusiveSections;
import subway.domain.vo.Section;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static subway.application.reader.CaseTypeSetter.setCase;

@Service
@Transactional(rollbackFor = {AddSectionException.class, SQLException.class})
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
        Reader reader = new InitializationReader(sectionDao);
        return reader.initializeSave(caseDto, allSections);
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
        final RequestInclusiveSections sections = sectionDao.findSectionByLineIdAndStationId(lineId, sectionId);

        if (sections.isEmpty()) {
            throw new IllegalArgumentException("해당하는 역이 없습니다.");
        }
        sectionDao.deleteSection(sections.getDepartureId());
        if (sections.isSizeTwo()) {
            sectionDao.deleteSection(sections.getArrivalId());
            sectionDao.saveSection(lineId, sections.getDistance(), sections.getDeparture(), sections.getArrival());
        }
    }
}
