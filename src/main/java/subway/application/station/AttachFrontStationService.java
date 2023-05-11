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
import subway.ui.dto.request.AttachFrontStationRequest;

@Transactional
@Service
public class AttachFrontStationService implements AttachFrontStationUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public AttachFrontStationService(final LineRepository lineRepository, final SectionRepository sectionRepository,
                                     final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public void attachEndStation(final Long lineId, final AttachFrontStationRequest request) {
        final Line line = lineRepository.findById(lineId);

        final Section section = createSection(lineId, request);

        final Sections sections = line.getSections();
        sections.attachAtFirstStation(
                new Station(request.getStandardStation()),
                new Station(request.getNewStation()),
                section.getDistance()
        );
    }

    private Section createSection(final Long lineId, final AttachFrontStationRequest request) {
        final Station standardStation = stationRepository.findByName(request.getStandardStation())
                .orElseThrow();

        final Station newStation = new Station(request.getNewStation());
        final StationDistance stationDistance = new StationDistance(request.getDistance());

        stationRepository.save(newStation);
        final Section newSection = new Section(standardStation, newStation, stationDistance);
        sectionRepository.save(newSection, lineId);

        return newSection;
    }
}
