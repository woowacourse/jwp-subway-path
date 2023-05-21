package subway.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.event.RouteUpdateEvent;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

@Service
public class SectionService {

    private final ApplicationEventPublisher publisher;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public SectionService(final ApplicationEventPublisher publisher, final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.publisher = publisher;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void insertSection(final SectionCreateRequest request) {
        Sections sections = sectionRepository.findSectionsByLineName(request.getLineName());
        Line line = lineRepository.findByLineNameWithSections(request.getLineName(), sections);

        Station requestUpStation = new Station(request.getUpStation());
        Station requestDownStation = new Station(request.getDownStation());
        Section section = new Section(requestUpStation, requestDownStation, request.getDistance());
        line.addSection(section);

        lineRepository.insertSectionInLine(sections, line.getLineNumber());

        publisher.publishEvent(new RouteUpdateEvent());
    }

    @Transactional
    public void deleteSection(final SectionDeleteRequest request) {
        long lineNumber = request.getLineNumber();
        Station station = new Station(request.getStation());
        Sections sections = sectionRepository.findSectionsByLineNumber(lineNumber);
        sections.deleteSectionByStation(station);

        lineRepository.insertSectionInLine(sections, lineNumber);

        publisher.publishEvent(new RouteUpdateEvent());
    }
}
