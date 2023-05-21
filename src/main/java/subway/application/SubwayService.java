package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.ShortestPath;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.AddLineRequest;
import subway.dto.AddStationRequest;
import subway.dto.DeleteStationRequest;
import subway.dto.LineResponse;
import subway.dto.PathResponse;
import subway.dto.SubwayPathRequest;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.SubwayRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Transactional
@Service
public class SubwayService {

    private final SubwayRepository subwayRepository;

    public SubwayService(SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    public long addStation(AddStationRequest addStationRequest) {
        Line line = getLine(addStationRequest.getLineName());
        Station stationToAdd = getStation(addStationRequest.getAddStationName()).orElseGet(() -> createNewStation(addStationRequest.getAddStationName()));
        Station upstream = getStation(addStationRequest.getUpstreamName()).orElseThrow(() -> new StationNotFoundException("추가하려는 역과 연결되는 상행역이 존재하지 않습니다."));
        Station downstream = getStation(addStationRequest.getDownstreamName()).orElseThrow(() -> new StationNotFoundException("추가하려는 역과 연결되는 하행역이 존재하지 않습니다."));
        line.addStation(stationToAdd, upstream, downstream, addStationRequest.getDistanceToUpstream());

        subwayRepository.updateLine(line);
        return subwayRepository.findStationIdByName(stationToAdd.getName())
                .orElseThrow(() -> new NoSuchElementException("디버깅: 역이 추가되어야 하는데 안됐습니다"));
    }

    private Line getLine(String lineNameInput) {
        LineName lineName = new LineName(lineNameInput);
        return subwayRepository.getLineByName(lineName);
    }

    private Optional<Station> getStation(String stationName) {
        Stations stations = subwayRepository.getStations();
        return stations.getStationByName(stationName);
    }

    private Station createNewStation(String stationName) {
        Station newStation = new Station(stationName);
        subwayRepository.addStation(newStation);
        return newStation;
    }

    public void deleteStation(DeleteStationRequest deleteStationRequest) {
        Line line = getLine(deleteStationRequest.getLineName());
        Station stationToDelete = getStation(deleteStationRequest.getStationName()).orElseThrow(() -> new StationNotFoundException("삭제하고자 하는 역이 존재하지 않습니다."));
        line.deleteStation(stationToDelete);

        subwayRepository.updateLine(line);
    }

    public long addNewLine(AddLineRequest addLineRequest) {
        LineName lineNameToAdd = new LineName(addLineRequest.getLineName());
        Station upstream = getStation(addLineRequest.getUpstreamName()).orElseGet(() -> createNewStation(addLineRequest.getUpstreamName()));
        Station downstream = getStation(addLineRequest.getDownstreamName()).orElseGet(() -> createNewStation(addLineRequest.getDownstreamName()));
        Section section = new Section(upstream, downstream, addLineRequest.getDistance());
        Line newLine = new Line(lineNameToAdd, section);
        Lines lines = subwayRepository.getLines();
        lines.addNewLine(newLine);
        return subwayRepository.addNewLine(newLine);
    }

    public LineResponse findLineById(long id) {
        Line line = subwayRepository.getLineById(id)
                .orElseThrow(() -> new LineNotFoundException("조회하고자 하는 노선이 없습니다"));
        return toLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return subwayRepository.getLines().getLines()
                .stream()
                .map(this::toLineResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private LineResponse toLineResponse(Line line) {
        return new LineResponse(line.getStationNamesInOrder(), line.getName().getName());
    }

    public PathResponse findShortestPath(SubwayPathRequest subwayPathRequest) {
        Lines lines = subwayRepository.getLines();
        Station departure = subwayRepository.findStation(subwayPathRequest.getDepartureId());
        Station destination = subwayRepository.findStation(subwayPathRequest.getDestinationId());
        ShortestPath shortestPath = lines.findShortestPath();
        return toPathResponse(shortestPath);
    }

    private PathResponse toPathResponse(ShortestPath shortestPath) {
        return shortestPath.getStations()
                .stream()
                .map(station -> station.getName())
                .collect(collectingAndThen(toList(), PathResponse::new));
    }
}
