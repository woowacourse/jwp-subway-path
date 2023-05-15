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

    public void deleteStationBy(Long stationId, Long lineId) {
        lineRepository.deleteStationBy(stationId, lineId);
    }
//
//    public void updateStationBy(Long stationId, Long lineId) {
//        lineRepository.updateStationBy(stationId, lineId);
//    }
}

//    private void  checkDuplicatedStationName(StationRequest stationRequest) { //특정노선에서 역 존재하면 예외처리
//        Line line = lineRepository.findByName(stationRequest.getLineName())
//                .orElseThrow(()-> new IllegalStateException("해당 노선이 존재하지 않습니다"));
//        boolean ifNewStationInLine = line.getSections().stream().
//                filter()
//        if(ifNewStationInLine) {
//            throw new IllegalArgumentException("이미 존재하는 역입니다.");
//        }
//    }
//
//    public StationResponse findStationResponseById(Long id) {
//        Station station = stationDao.findById(id).orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
//        return StationResponse.of(station);
//    }
//
//    public List<StationResponse> findAllStationResponses() {
//        List<Station> stations = stationDao.findAll();
//
//        return stations.stream()
//                .map(StationResponse::of)
//                .collect(Collectors.toList());
//    }
//
//    public void updateStation(Long id, StationRequest stationRequest) {
//        stationDao.update(new Station(id, stationRequest.getName()));
//    }
//
//    public void deleteStationById(Long id) {
//        stationDao.deleteById(id);
//    }
