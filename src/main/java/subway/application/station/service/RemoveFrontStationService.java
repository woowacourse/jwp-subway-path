package subway.application.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.usecase.RemoveFrontStationUseCase;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.ui.dto.request.RemoveStationRequest;

@Transactional
@Service
public class RemoveFrontStationService implements RemoveFrontStationUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public RemoveFrontStationService(final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public void removeFrontStation(final Long lineId, final RemoveStationRequest request) {
        final Line line = lineRepository.findById(lineId);
        final Station removeStation = new Station(request.getStation());
        final Sections sections = line.getSections();
        final Section removedSection = sections.peekByFirstStationUnique(removeStation);

        sections.removeFirstStation(removeStation);

        sectionRepository.delete(removedSection);
    }
}
