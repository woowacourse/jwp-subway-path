package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.port.input.RemoveEndStationUseCase;
import subway.line.domain.Line;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.application.port.output.SectionRepository;
import subway.section.domain.Sections;
import subway.station.domain.Station;
import subway.ui.dto.request.RemoveStationRequest;

@Transactional
@Service
public class RemoveEndStationService implements RemoveEndStationUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public RemoveEndStationService(final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void removeEndStation(final Long lineId, final RemoveStationRequest request) {
        final Line line = lineRepository.findById(lineId);
        final Station removeStation = new Station(request.getStation());
        final Sections sections = line.getSections();
        final Section removedSection = sections.peekBySecondStationUnique(removeStation);

        sections.removeLastStation(removeStation);

        sectionRepository.delete(removedSection);
    }
}
