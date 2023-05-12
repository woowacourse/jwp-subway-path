package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.application.port.output.SectionRepository;
import subway.section.domain.Sections;
import subway.station.application.port.input.RemoveBetweenStationUseCase;
import subway.station.domain.Station;
import subway.ui.dto.request.RemoveStationRequest;

@Transactional
@Service
public class RemoveBetweenStationService implements RemoveBetweenStationUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public RemoveBetweenStationService(final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void removeBetweenStation(final Long lineId, final RemoveStationRequest request) {
        final Line line = lineRepository.findById(lineId);
        final Station removeStation = new Station(request.getStation());
        final Sections sections = line.getSections();
        final Section leftSection = sections.peekBySecondStationUnique(removeStation);
        final Section rightSection = sections.peekByFirstStationUnique(removeStation);

        sections.removeStation(removeStation);
        sectionRepository.delete(leftSection);
        sectionRepository.delete(rightSection);

        final Section udpatedSection = sections.peekByFirstStationUnique(leftSection.getFirstStation());
        sectionRepository.save(udpatedSection, lineId);
    }
}
