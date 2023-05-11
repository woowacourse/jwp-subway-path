package subway.application.station.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.usecase.AttachEndStationUseCase;
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
        final StationDistance stationDistance = new StationDistance(request.getDistance());

        final Sections sections = line.getSections();
        sections.attachAtLastStation(standardStation, newStation, stationDistance);

        final Section newSection = new Section(standardStation, newStation, stationDistance);
        sectionRepository.save(newSection, lineId);
        saveIfNotExist(newStation);
    }

    private void saveIfNotExist(final Station station) {
        final Optional<Station> findByNameStation =
                stationRepository.findByName(station.getStationName());

        if (findByNameStation.isEmpty()) {
            stationRepository.save(station);
        }
    }
}
