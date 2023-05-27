package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Transactional
@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(final SectionRepository sectionRepository, final StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public void addSection(final SectionCreateRequest request) {
        final Long requestLineNumber = request.getLineNumber();
        final Sections sections = sectionRepository.findByLineNumber(requestLineNumber);

        final Station requestUpStation = stationRepository.findByName(request.getUpStation());
        final Station requestDownStation = stationRepository.findByName(request.getDownStation());
        final Section requestSection = new Section(requestUpStation, requestDownStation, request.getDistance());
        sections.addSection(requestSection);

        sectionRepository.updateByLineNumber(sections, requestLineNumber);
    }

    public void removeSection(final SectionDeleteRequest request) {
        final Long requestLineNumber = request.getLineNumber();
        final Station requestStation = stationRepository.findByName(request.getStation());

        final Sections sections = sectionRepository.findByLineNumber(requestLineNumber);
        sections.deleteSectionByStation(requestStation);

        sectionRepository.updateByLineNumber(sections, requestLineNumber);
    }
}
