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
import subway.exception.line.LineIsInitException;
import subway.exception.line.LineIsNotInitException;
import subway.repository.LineRepository;

import java.util.List;

@Service
public class StationService {

    private static final int UPBOUND_STATION = 0;
    private static final int DOWNBOUND_STATION = 1;

    private final LineRepository lineRepository;

    public StationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public StationInitResponse saveInitStations(final StationInitRequest stationInitRequest) {
        Line line = lineRepository.findByName(stationInitRequest.getLineName());
        if (!line.isEmpty()) {
            throw new LineIsNotInitException();
        }

        Section section = new Section(
                new Station(stationInitRequest.getUpBoundStationName()),
                new Station(stationInitRequest.getDownBoundStationName()),
                stationInitRequest.getDistance());
        List<Station> stations = lineRepository.saveInitStations(section, line.getId());

        return new StationInitResponse(
                stations.get(UPBOUND_STATION).getId(),
                stations.get(UPBOUND_STATION).getName(),
                stations.get(DOWNBOUND_STATION).getId(),
                stations.get(DOWNBOUND_STATION).getName());
    }

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        Line line = lineRepository.findByName(stationRequest.getLineName());
        if (line.isEmpty()) {
            throw new LineIsInitException();
        }

        Station baseStation = lineRepository.findByNameAndLineId(stationRequest.getBaseStation(), line.getId());
        Station insertStation = new Station(stationRequest.getRegisterStationName());
        Station insertedStation = lineRepository.saveStation(insertStation, line.getId());

        if (line.isBoundStation(baseStation)) {
            updateBoundSection(stationRequest, line, baseStation, insertedStation);
            return new StationResponse(insertedStation.getId(), insertedStation.getName());
        }
        updateInterSection(stationRequest, line, baseStation, insertedStation);
        return new StationResponse(insertedStation.getId(), insertedStation.getName());
    }

    private void updateBoundSection(StationRequest stationRequest, Line line, Station baseStation, Station insertedStation) {
        if (stationRequest.getDirection().equals("left")) {
            line.addUpBoundStation(baseStation, insertedStation);
            lineRepository.updateBoundSection(line.getId(), baseStation, insertedStation, stationRequest.getDirection(), stationRequest.getDistance());
            return;
        }
        if (stationRequest.getDirection().equals("right")) {
            line.addDownBoundStation(baseStation, insertedStation);
            lineRepository.updateBoundSection(line.getId(), baseStation, insertedStation, stationRequest.getDirection(), stationRequest.getDistance());
            return;
        }
        throw new IllegalArgumentException("잘못된 방향이 입력되었습니다.");
    }

    private void updateInterSection(StationRequest stationRequest, Line line, Station baseStation, Station insertStation) {
        line.addInterStation(baseStation, insertStation, stationRequest.getDirection(), stationRequest.getDistance());
        Section section = line.findSection(baseStation, stationRequest.getDirection());

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
        throw new IllegalArgumentException("잘못된 방향이 입력되었습니다.");
    }
}
