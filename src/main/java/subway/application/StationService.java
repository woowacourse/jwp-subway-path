package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.dto.StationInitRequest;
import subway.dto.StationInitResponse;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.input.InvalidDirectionException;
import subway.exception.line.LineIsInitException;
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
        if (line.isEmpty()) {
            throw new LineIsInitException();
        }
        Station insertedStation = lineRepository.saveStation(new Station(stationRequest.getRegisterStationName()), line.getId());
        line.validateAlreadyExistStation(insertedStation);
        Station baseStation = lineRepository.findByNameAndLineId(stationRequest.getBaseStation(), line.getId());

        if (line.isUpBoundStation(baseStation)) {
            updateUpBoundSection(stationRequest, line, baseStation, insertedStation, insertedStation);
            return new StationResponse(insertedStation.getId(), insertedStation.getName());
        }

        if (line.isDownBoundStation(baseStation)) {
            updateDownBoundSection(stationRequest, line, baseStation, insertedStation, insertedStation);
            return new StationResponse(insertedStation.getId(), insertedStation.getName());
        }

        updateInterSection(stationRequest, line, baseStation, insertedStation);
        return new StationResponse(insertedStation.getId(), insertedStation.getName());
    }

    private void updateUpBoundSection(StationRequest stationRequest, Line line, Station baseStation, Station insertStation, Station insertedStation) {
        if (stationRequest.getDirection().equals("left")) {
            lineRepository.updateBoundSection(line.getId(), baseStation, insertedStation, stationRequest.getDirection(), stationRequest.getDistance());
            return;
        }
        if (stationRequest.getDirection().equals("right")) {
            Section boundSection = line.findSectionByBoundStation(baseStation);
            line.validateDistanceLength(boundSection, stationRequest.getDistance());
            List<Section> sections = List.of(
                    new Section(boundSection.getLeftStation(), insertedStation, stationRequest.getDistance()),
                    new Section(insertStation, boundSection.getRightStation(), boundSection.calculateDistance(stationRequest.getDistance()))
            );
            lineRepository.updateInterSection(line.getId(), boundSection, sections);
            return;
        }
        throw new InvalidDirectionException();
    }

    private void updateDownBoundSection(StationRequest stationRequest, Line line, Station baseStation, Station insertStation, Station insertedStation) {
        if (stationRequest.getDirection().equals("left")) {
            Section boundSection = line.findSectionByBoundStation(baseStation);
            line.validateDistanceLength(boundSection, stationRequest.getDistance());
            List<Section> sections = List.of(
                    new Section(boundSection.getLeftStation(), insertedStation, boundSection.calculateDistance(stationRequest.getDistance())),
                    new Section(insertStation, boundSection.getRightStation(), stationRequest.getDistance())
            );
            lineRepository.updateInterSection(line.getId(), boundSection, sections);
            return;
        }
        if (stationRequest.getDirection().equals("right")) {
            lineRepository.updateBoundSection(line.getId(), baseStation, insertedStation, stationRequest.getDirection(), stationRequest.getDistance());
            return;
        }
        throw new InvalidDirectionException();
    }

    private void updateInterSection(StationRequest stationRequest, Line line, Station baseStation, Station insertStation) {
        Section section = line.findSection(baseStation, stationRequest.getDirection());
        line.validateDistanceLength(section, stationRequest.getDistance());

        if (stationRequest.getDirection().equals("left")) {
            List<Section> sections = List.of(
                    new Section(section.getLeftStation(), insertStation, section.calculateDistance(stationRequest.getDistance())),
                    new Section(insertStation, section.getRightStation(), stationRequest.getDistance())
            );
            lineRepository.updateInterSection(line.getId(), section, sections);
            return;
        }
        if (stationRequest.getDirection().equals("right")) {
            List<Section> sections = List.of(
                    new Section(insertStation, section.getRightStation(), stationRequest.getDistance()),
                    new Section(section.getLeftStation(), insertStation, section.calculateDistance(stationRequest.getDistance()))
            );
            lineRepository.updateInterSection(line.getId(), section, sections);
            return;
        }
        throw new InvalidDirectionException();
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
