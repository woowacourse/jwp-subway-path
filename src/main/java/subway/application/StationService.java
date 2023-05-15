package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.ui.dto.StationRequest;


@Service
public class StationService {

    private final LineRepository lineRepository;

    public StationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void addStation(StationRequest request) {
        Line line = lineRepository.findLineByName(request.getLineName())
                .orElseThrow(() -> new IllegalStateException("해당 노선이 존재하지 않습니다"));
        line.addStation(request.getBaseStationName(), request.getNewStationName(), request.getDirectionOfBaseStation(),
                request.getDistance());
        if (line.isEmpty()) {
            addInitialStations(line, request);
            return;
        }
        addNewStation(line, request);
    }

    private void addInitialStations(Line line, StationRequest request) {
        Long baseStationId = lineRepository.addNewStationToLine(request.getBaseStationName(), line.getId());
        Long newStationId = lineRepository.addNewStationToLine(request.getNewStationName(), line.getId());
        lineRepository.addNewSectionToLine(
                new Section(new Station(baseStationId, request.getBaseStationName()),
                        new Station(newStationId, request.getNewStationName()),
                        new Distance(request.getDistance())),
                line.getId());
    }

    private void addNewStation(Line line, StationRequest request) {
        Long lineId = line.getId();
        Section sectionToRemove = findOriginSection(request, lineId);
        lineRepository.deleteSectionInLine(sectionToRemove, lineId);

        lineRepository.addNewStationToLine(request.getNewStationName(), lineId);
        line.findSectionsByStationName(request.getNewStationName())
                .forEach(section -> lineRepository.addNewSectionToLine(section, lineId));
    }


    private Section findOriginSection(StationRequest request, Long lineId) {
        if (Direction.LEFT.isSameDirection(request.getDirectionOfBaseStation())) {
            return lineRepository.findSectionByUpStation(request.getBaseStationName(), lineId);
        }
        return lineRepository.findSectionByDownStation(request.getBaseStationName(), lineId);
    }

    public List<Station> findAllStations() {
        return lineRepository.findAllStations();
    }

    public Station findStationBy(Long stationId, Long lineId) {
        return lineRepository.findStationById(stationId, lineId);
    }

    //line 도메인 이용해서 수정해야 함
    public void deleteStationBy(Long stationId, Long lineId) {
        lineRepository.deleteStationBy(stationId, lineId);
    }

}

