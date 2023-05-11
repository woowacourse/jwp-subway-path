package subway.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.persistence.dao.LineDao;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.section.dto.AddResultDto;
import subway.service.section.dto.SectionCreateRequest;
import subway.service.section.dto.SectionCreateResponse;
import subway.service.section.dto.SectionResponse;
import subway.service.section.repository.SectionRepository;
import subway.service.station.domain.Station;

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
