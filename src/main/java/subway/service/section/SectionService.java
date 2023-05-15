package subway.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.section.dto.LineStationDeleteRequest;
import subway.persistence.repository.SectionRepositoryImpl;
import subway.service.line.LineRepository;
import subway.service.line.domain.Line;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.section.dto.AddResult;
import subway.service.section.dto.DeleteResult;
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

    public SectionService(LineRepository lineRepository, SectionRepositoryImpl sectionRepository, StationRepository stationDao) {
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
        for (Section section : addResult.getAddedResults()) {
            Section savedSection = sectionRepository.insertSection(section, line);
            addedSectionResponses.add(SectionResponse.of(savedSection));
        }

        for (Section section : addResult.getDeletedResults()) {
            sectionRepository.deleteSection(section);
        }

        return new SectionCreateResponse(sectionCreateRequest.getLineId(), addedSectionResponses, List.of());

    }

    public void delete(LineStationDeleteRequest lineStationDeleteRequest) {
        Station station = stationRepository.findById(lineStationDeleteRequest.getStationId());
        Map<Line, Sections> sectionsPerLine = sectionRepository.findSectionsByStation(station);

        for (Line perLine : sectionsPerLine.keySet()) {
            Sections sections = sectionsPerLine.get(perLine);
            boolean isLastSection = sectionRepository.isLastSectionInLine(perLine);
            if (isLastSection) {
                Section lastSection = sections.getSections().get(0);
                deleteStationsInLastSection(station, lastSection);
                continue;
            }
            DeleteResult deleteResult = sections.deleteSection(station);

            for (Section addedSection : deleteResult.getAddedSections()) {
                sectionRepository.insertSection(addedSection, perLine);
            }
        }
        stationRepository.deleteById(station.getId());

    }

    private void deleteStationsInLastSection(Station station, Section lastSection) {
        if (lastSection.isUpStation(station)) {
            stationRepository.deleteById(lastSection.getDownStation().getId());
            return;
        }
        stationRepository.deleteById(lastSection.getUpStation().getId());
    }
}
