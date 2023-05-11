package subway.application.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationDistance;
import subway.domain.station.StationRepository;
import subway.ui.dto.request.AttachStationRequest;

@Transactional
@Service
public class AttachBetweenStationService implements AttachBetweenStationUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public AttachBetweenStationService(final LineRepository lineRepository, final SectionRepository sectionRepository,
                                       final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public void attachBetweenStation(final Long lineId, final AttachStationRequest request) {
        final Line line = lineRepository.findById(lineId);
        final Sections sections = line.getSections();

        final Station newStation = new Station(request.getNewStation());
        final Station standardStation = new Station(request.getStandardStation());
        stationRepository.save(newStation);
        final Section removedSection = sections.peekByFirstStationUnique(standardStation);

        sections.insertBehindStation(standardStation, newStation, new StationDistance(request.getDistance()));

        sectionRepository.delete(removedSection);

        final Section sectionA = sections.peekByFirstStationUnique(standardStation);
        final Section sectionB = sections.peekByFirstStationUnique(newStation);
        sectionRepository.save(sectionA, lineId);
        sectionRepository.save(sectionB, lineId);
    }
}
