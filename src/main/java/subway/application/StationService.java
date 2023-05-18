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

import java.util.List;

@Service
public class StationService {

    private final LineRepository lineRepository;

    public StationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public StationInitResponse saveInitStations(final StationInitRequest stationInitRequest) {
        Line line = lineRepository.findByName(stationInitRequest.getLineName());

        Section section = new Section(
                new Station(stationInitRequest.getUpBoundStationName()),
                new Station(stationInitRequest.getDownBoundStationName()),
                stationInitRequest.getDistance());

        Section addedSection = line.addInitStations(section);
        List<Station> stations = lineRepository.saveInitStations(addedSection, line.getId());

        return new StationInitResponse(stations);
    }

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        Line line = lineRepository.findByName(stationRequest.getLineName());
        Station baseStation = lineRepository.findByNameAndLineId(stationRequest.getBaseStation(), line.getId());
        Station registerStation = lineRepository.saveStation(new Station(stationRequest.getRegisterStationName()), line.getId());

        Sections updatedsections = line.addStation(baseStation, stationRequest.getDirection(), registerStation, stationRequest.getDistance());
        lineRepository.updateSection(updatedsections, line.getId());

        return new StationResponse(registerStation.getId(), registerStation.getName());
    }

    @Transactional
    public void deleteStation(Long stationId) {
        Line line = lineRepository.findByStationId(stationId);
        Station station = lineRepository.findStationById(stationId);

        if (line.isBoundStation(station)) {
            deleteBoundStation(line, station);
            return;
        }

        List<Section> sectionsWithStation = line.findSectionByInterStation(station);
        Section section = line.updateSection(sectionsWithStation);
        lineRepository.updateSectionAndDeleteStation(line.getId(), sectionsWithStation, section, station);
    }

    private void deleteBoundStation(Line line, Station station) {
        Section section = line.findSectionByBoundStation(station);
        if (line.hasOneSection()) {
            lineRepository.deleteSectionAndAllStation(section);
            return;
        }
        lineRepository.deleteSectionAndStation(section, station);
    }
}
