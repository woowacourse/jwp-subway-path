package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.repository.SectionRepository;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void insertSection(final SectionCreateRequest request) {
        final Long requestLineNumber = request.getLineNumber();
        final Sections sections = sectionRepository.findSectionsByLineNumber(requestLineNumber);

        final Station requestUpStation = new Station(request.getUpStation());
        final Station requestDownStation = new Station(request.getDownStation());
        final Section requestSection = new Section(requestUpStation, requestDownStation, request.getDistance());
        sections.addSection(requestSection);

        sectionRepository.updateSectionsByLineNumber(sections, requestLineNumber);
    }

    @Transactional
    public void deleteSection(final SectionDeleteRequest request) {
        final Long requestLineNumber = request.getLineNumber();
        final Station requestStation = new Station(request.getStation());

        final Sections sections = sectionRepository.findSectionsByLineNumber(requestLineNumber);
        sections.deleteSectionByStation(requestStation);

        sectionRepository.updateSectionsByLineNumber(sections, requestLineNumber);
    }
}
