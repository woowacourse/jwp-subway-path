package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.StationInitRequest;
import subway.dto.StationInitResponse;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;

@Service
public class StationService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public StationService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationInitResponse saveInitStations(final StationInitRequest stationInitRequest) {
        Line line = lineRepository.findByName(stationInitRequest.getLineName());

        Section section = new Section(
                new Station(stationInitRequest.getUpBoundStationName()),
                new Station(stationInitRequest.getDownBoundStationName()),
                stationInitRequest.getDistance());

        Section addedSection = line.addInitStations(section);
        List<Station> stations = sectionRepository.saveInitStations(addedSection, line.getId());

        return new StationInitResponse(stations);
    }

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        Line line = lineRepository.findByName(stationRequest.getLineName());
        Station baseStation = stationRepository.findByNameAndLineId(stationRequest.getBaseStation(), line.getId());
        Station registerStation = stationRepository.save(new Station(stationRequest.getRegisterStationName()), line.getId());

        Sections updatedsections = line.addStation(baseStation, stationRequest.getDirection(), registerStation, stationRequest.getDistance());
        sectionRepository.updateSection(updatedsections, line.getId());

        return new StationResponse(registerStation.getId(), registerStation.getName());
    }

    @Transactional
    public void deleteStation(Long stationId) {
        Line line = lineRepository.findByStationId(stationId);
        Station station = stationRepository.findById(stationId);

        Sections updatedSections = line.deleteStation(station);

        sectionRepository.updateSectionAndDeleteStation(line.getId(), updatedSections, station);
    }
}
