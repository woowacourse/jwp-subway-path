package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.port.input.AttachBetweenStationUseCase;
import subway.line.domain.Line;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Section;
import subway.section.application.port.output.SectionRepository;
import subway.section.domain.Sections;
import subway.station.domain.Station;
import subway.station.domain.StationDistance;
import subway.station.application.port.output.StationRepository;
import subway.ui.dto.request.AttachStationRequest;

import java.util.Optional;

@Transactional
@Service
public class AttachBetweenStationService implements AttachBetweenStationUseCase {
    
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    
    public AttachBetweenStationService
            (
                    final LineRepository lineRepository,
                    final SectionRepository sectionRepository,
                    final StationRepository stationRepository
            ) {
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
        saveIfNotExist(newStation);
        final Section removedSection = sections.peekByFirstStationUnique(standardStation);
        
        sections.insertBehindStation(standardStation, newStation, new StationDistance(request.getDistance()));
        
        sectionRepository.delete(removedSection);
        
        final Section leftSection = sections.peekByFirstStationUnique(standardStation);
        final Section rightSection = sections.peekByFirstStationUnique(newStation);
        sectionRepository.save(leftSection, lineId);
        sectionRepository.save(rightSection, lineId);
    }
    
    private void saveIfNotExist(final Station station) {
        final Optional<Station> findByNameStation =
                stationRepository.findByName(station.getStationName());
        
        if (findByNameStation.isEmpty()) {
            stationRepository.save(station);
        }
    }
}
