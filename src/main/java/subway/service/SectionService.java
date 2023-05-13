package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.repository.SectionRepository;

@Transactional
@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(final SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public void insertSection(final SectionCreateRequest request) {
        final Long requestLineNumber = request.getLineNumber();
        final Sections sections = sectionRepository.findByLineNumber(requestLineNumber);

        final Station requestUpStation = new Station(request.getUpStation());
        final Station requestDownStation = new Station(request.getDownStation());
        final Section requestSection = new Section(requestUpStation, requestDownStation, request.getDistance());
        sections.addSection(requestSection);

        sectionRepository.updateByLineNumber(sections, requestLineNumber);
    }

    public void deleteSection(final SectionDeleteRequest request) {
        final Long requestLineNumber = request.getLineNumber();
        final Station requestStation = new Station(request.getStation());

        final Sections sections = sectionRepository.findByLineNumber(requestLineNumber);
        sections.deleteSectionByStation(requestStation);

        sectionRepository.updateByLineNumber(sections, requestLineNumber);
    }
}
