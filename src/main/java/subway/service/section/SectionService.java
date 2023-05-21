package subway.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.section.dto.LineStationDeleteRequest;
import subway.persistence.repository.H2SectionRepository;
import subway.service.line.LineRepository;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.section.dto.AddResult;
import subway.service.section.dto.SectionCreateRequest;
import subway.service.section.dto.SectionCreateResponse;
import subway.service.section.dto.SectionResponse;
import subway.service.section.repository.SectionRepository;
import subway.service.station.StationRepository;
import subway.service.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(LineRepository lineRepository, H2SectionRepository sectionRepository, StationRepository stationDao) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationDao;
    }

    public SectionCreateResponse insert(SectionCreateRequest sectionCreateRequest) {
        Line line = lineRepository.findById(sectionCreateRequest.getLineId());
        Sections sections = sectionRepository.findSectionsByLine(line);
        Station upStation = stationRepository.findById(sectionCreateRequest.getUpStationId());
        Station downStation = stationRepository.findById(sectionCreateRequest.getDownStationId());
        Distance distance = new Distance(sectionCreateRequest.getDistance());

        AddResult addResult = sections.add(upStation, downStation, distance);
        List<SectionResponse> addedSectionResponses = new ArrayList<>();
        insertNewSection(line, addResult, addedSectionResponses);
        deleteExistSection(addResult);

        return new SectionCreateResponse(sectionCreateRequest.getLineId(), addedSectionResponses, List.of());

    }

    public void delete(LineStationDeleteRequest lineStationDeleteRequest) {
        Station station = stationRepository.findById(lineStationDeleteRequest.getStationId());
        Map<Line, Sections> sectionsPerLine = sectionRepository.findSectionsByStation(station);

        for (Line perLine : sectionsPerLine.keySet()) {
            Sections sections = sectionsPerLine.get(perLine);
            deleteStationLastSection(station, perLine, sections);
        }
        stationRepository.deleteById(station.getId());
    }

    private void deleteExistSection(AddResult addResult) {
        for (Section section : addResult.getDeletedResults()) {
            sectionRepository.deleteSection(section);
        }
    }

    private void insertNewSection(Line line, AddResult addResult, List<SectionResponse> addedSectionResponses) {
        for (Section section : addResult.getAddedResults()) {
            Section savedSection = sectionRepository.insertSection(section, line);
            addedSectionResponses.add(SectionResponse.from(savedSection));
        }
    }

    private void deleteStationLastSection(Station station, Line perLine, Sections sections) {
        boolean isLastSection = sectionRepository.isLastSectionInLine(perLine);
        if (isLastSection) {
            Section lastSection = sections.getSections().get(0);
            deleteStationsInLastSection(station, lastSection);
        }
    }

    private void deleteStationsInLastSection(Station station, Section lastSection) {
        if (lastSection.isUpStation(station)) {
            stationRepository.deleteById(lastSection.getDownStation().getId());
            return;
        }
        stationRepository.deleteById(lastSection.getUpStation().getId());
    }
}
