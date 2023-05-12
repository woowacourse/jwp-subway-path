package subway.station.application;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.port.input.CreateInitialStationsUseCase;
import subway.line.domain.Line;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.application.port.output.SectionRepository;
import subway.section.domain.Sections;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;
import subway.station.application.port.output.StationRepository;
import subway.ui.dto.request.CreateInitialStationsRequest;

@Transactional
@Service
public class CreateInitialStationsService implements CreateInitialStationsUseCase {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public CreateInitialStationsService(final LineRepository lineRepository, final SectionRepository sectionRepository,
                                        final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Long addInitialStations(final Long lineId, final CreateInitialStationsRequest request) {
        final Line line = lineRepository.findById(lineId);
        final Sections sections = line.getSections();

        final Section initialSection = createSectionBy(request);

        sections.addInitialStations(initialSection);
        return sectionRepository.save(initialSection, lineId);
    }

    private Section createSectionBy(final CreateInitialStationsRequest request) {
        final Station firstStation = new Station(request.getFirstStation());
        final Station secondStation = new Station(request.getSecondStation());

        saveIfNotExist(new Station(request.getFirstStation()));
        saveIfNotExist(new Station(request.getSecondStation()));

        return new Section(
                firstStation,
                secondStation,
                new StationDistance(request.getDistance())
        );
    }

    private void saveIfNotExist(final Station station) {
        final Optional<Station> findByNameStation =
                stationRepository.findByName(station.getStationName());

        if (findByNameStation.isEmpty()) {
            stationRepository.save(station);
        }
    }
}
