package subway.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.section.dto.LineStationDeleteRequest;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.StationDao;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.section.dto.AddResult;
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
    private final StationDao stationDao;

    public SectionService(LineDao lineDao, SectionRepository sectionRepository, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
        this.stationDao = stationDao;
    }

    public SectionCreateResponse insert(SectionCreateRequest sectionCreateRequest) {
        Line line = lineDao.findById(sectionCreateRequest.getLineId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        Sections sections = sectionRepository.findSectionsByLine(line);
        Station upStation = findStationById(sectionCreateRequest.getUpStationId());
        Station downStation = findStationById(sectionCreateRequest.getDownStationId());
        Distance distance = new Distance(sectionCreateRequest.getDistance());

        AddResult addResult = sections.add(upStation, downStation, distance);
        List<SectionResponse> addedSectionResponses = new ArrayList<>();
        for (Section section : addResult.getAddedResults()) {
            Section savedSection = sectionRepository.insertSection(section, line);
            addedSectionResponses.add(SectionResponse.of(savedSection));
        }

//        for (Section deletedResult : addResult.getDeletedResults()) {
//            1
//        }

        return new SectionCreateResponse(sectionCreateRequest.getLineId(), addedSectionResponses, List.of());

    }

    public void delete(LineStationDeleteRequest lineStationDeleteRequest) {
        Line line = lineDao.findById(lineStationDeleteRequest.getLineId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        Sections sections = sectionRepository.findSectionsByLine(line);
        Station station = findStationById(lineStationDeleteRequest.getStationId());

        sections.deleteSection(station);
    }

    private Station findStationById(long id) {
        return stationDao.findById(id);
    }
}
