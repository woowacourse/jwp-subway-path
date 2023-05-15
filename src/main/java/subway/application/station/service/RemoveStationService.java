package subway.application.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.usecase.RemoveStationUseCase;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;

@Transactional
@Service
public class RemoveStationService implements RemoveStationUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public RemoveStationService(
            final LineRepository lineRepository,
            final SectionRepository sectionRepository,
            final StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public void removeStation(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findById(lineId);
        final Sections sections = line.getSections();
        final Station removalStation = stationRepository.findById(stationId);
        if (sections.getFrontStation().equals(removalStation)) {
            removeSection(sections, sections.peekByFirstStationUnique(removalStation));
            return;
        }

        if (sections.getEndStation().equals(removalStation)) {
            removeSection(sections, sections.peekBySecondStationUnique(removalStation));
            return;
        }

        removeMiddleStation(lineId, sections, removalStation);
    }

    private void removeMiddleStation(final Long lineId, final Sections sections, final Station removalStation) {
        final Section leftSection = sections.peekBySecondStationUnique(removalStation);
        final Section rightSection = sections.peekByFirstStationUnique(removalStation);

        removeSection(sections, leftSection);
        removeSection(sections, rightSection);

        final Section mergedSection = leftSection.merge(rightSection);
        sections.addSection(mergedSection);
        sectionRepository.save(mergedSection, lineId);
    }


    private void removeSection(final Sections sections, final Section removalSection) {
        sections.removeSection(removalSection);
        sectionRepository.delete(removalSection);
    }
}
