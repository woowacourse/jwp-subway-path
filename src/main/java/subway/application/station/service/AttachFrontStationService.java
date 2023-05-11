package subway.application.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.usecase.AttachFrontStationUseCase;
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
    public void attachFrontStation(final Long lineId, final AttachStationRequest request) {
        final Line line = lineRepository.findById(lineId);

        final Station standardStation = new Station(request.getStandardStation());
        final Station newStation = new Station(request.getNewStation());
        final StationDistance stationDistance = new StationDistance(request.getDistance());

        final Sections sections = line.getSections();
        sections.attachAtFirstStation(standardStation, newStation, stationDistance);

        final Section newSection = new Section(newStation, standardStation, stationDistance);
        sectionRepository.save(newSection, lineId);
        stationRepository.save(newStation);
    }
}
