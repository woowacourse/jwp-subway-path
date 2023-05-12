package subway.station.application;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.port.input.AttachEndStationUseCase;
import subway.line.domain.Line;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.application.port.output.SectionRepository;
import subway.section.domain.Sections;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;
import subway.station.application.port.output.StationRepository;
import subway.ui.dto.request.AttachStationRequest;

@Transactional
@Service
public class AttachEndStationService implements AttachEndStationUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public AttachEndStationService(final LineRepository lineRepository, final SectionRepository sectionRepository,
                                   final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public void attachEndStation(final Long lineId, final AttachStationRequest request) {
        final Line line = lineRepository.findById(lineId);

        final Station standardStation = new Station(request.getStandardStation());
        final Station newStation = new Station(request.getNewStation());
        saveIfNotExist(newStation);
        final StationDistance stationDistance = new StationDistance(request.getDistance());

        final Sections sections = line.getSections();
        sections.attachAtLastStation(standardStation, newStation, stationDistance);

        final Section newSection = new Section(standardStation, newStation, stationDistance);
        sectionRepository.save(newSection, lineId);
    }

    private void saveIfNotExist(final Station station) {
        final Optional<Station> findByNameStation =
                stationRepository.findByName(station.getStationName());

        if (findByNameStation.isEmpty()) {
            stationRepository.save(station);
        }
    }
}
