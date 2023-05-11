package subway.application.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.usecase.RemoveBetweenStationUseCase;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
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
