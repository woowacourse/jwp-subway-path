package subway.service.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.persistence.repository.section.JdbcSectionRepository;
import subway.persistence.repository.station.JdbcStationRepository;

@Transactional
@Service
public class SectionService {

    private final JdbcSectionRepository jdbcSectionRepository;
    private final JdbcStationRepository jdbcStationRepository;

    public SectionService(final JdbcSectionRepository jdbcSectionRepository, final JdbcStationRepository jdbcStationRepository) {
        this.jdbcSectionRepository = jdbcSectionRepository;
        this.jdbcStationRepository = jdbcStationRepository;
    }

    public void addSection(final SectionCreateRequest request) {
        final Long requestLineNumber = request.getLineNumber();
        final Sections sections = jdbcSectionRepository.findByLineNumber(requestLineNumber);

        final Station requestUpStation = jdbcStationRepository.findByName(request.getUpStation());
        final Station requestDownStation = jdbcStationRepository.findByName(request.getDownStation());
        final Section requestSection = new Section(requestUpStation, requestDownStation, request.getDistance());
        sections.addSection(requestSection);

        jdbcSectionRepository.updateByLineNumber(sections, requestLineNumber);
    }

    public void removeSection(final SectionDeleteRequest request) {
        final Long requestLineNumber = request.getLineNumber();
        final Station requestStation = jdbcStationRepository.findByName(request.getStation());

        final Sections sections = jdbcSectionRepository.findByLineNumber(requestLineNumber);
        sections.deleteSectionByStation(requestStation);

        jdbcSectionRepository.updateByLineNumber(sections, requestLineNumber);
    }
}
