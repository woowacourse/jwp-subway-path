package subway.application.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.section.dto.SectionCreateRequest;
import subway.application.section.dto.SectionCreateResponse;
import subway.application.section.dto.SectionResponse;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.repository.SectionRepository;
import subway.dto.AddResultDto;
import subway.persistence.dao.LineDao;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SectionService {

    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public SectionService(LineDao lineDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public SectionCreateResponse insert(SectionCreateRequest sectionCreateRequest, long lineId) {
        Line line = lineDao.findById(lineId);
        Sections sections = sectionRepository.findSectionsByLine(line);
        Station upStation = new Station(sectionCreateRequest.getUpStationName());
        Station downStation = new Station(sectionCreateRequest.getDownStationName());
        Distance distance = new Distance(sectionCreateRequest.getDistance());

        AddResultDto addResult = sections.add(upStation, downStation, distance);
        List<SectionResponse> addedSectionResponses = new ArrayList<>();
        for (Section section : addResult.getAddedResults()) {
            Section savedSection = sectionRepository.insertSection(section, line);
            addedSectionResponses.add(SectionResponse.of(savedSection));
        }

        return new SectionCreateResponse(lineId, addedSectionResponses, List.of());

    }
}
