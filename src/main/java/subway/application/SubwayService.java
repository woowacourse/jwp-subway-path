package subway.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.*;
import subway.dto.AddLineRequest;
import subway.dto.AddStationRequest;
import subway.dto.DeleteStationRequest;
import subway.dto.LineResponse;
import subway.exception.LineNotFoundException;
import subway.repository.SubwayRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional
@Service
public class SubwayService {

    private final SubwayRepository subwayRepository;

    @Autowired
    public SubwayService(SubwayRepository subwayRepository) {
        this.subwayRepository = subwayRepository;
    }

    public long addStation(AddStationRequest addStationRequest) {
        Line line = getLine(addStationRequest.getLineName());
        Station stationToAdd = Station.from(addStationRequest.getAddStationName());
        Station upstream = Station.from(addStationRequest.getUpstreamName());
        Station downstream = Station.from(addStationRequest.getDownstreamName());
        line.addStation(stationToAdd, upstream, downstream, addStationRequest.getDistanceToUpstream());

        subwayRepository.addStation(stationToAdd);
        subwayRepository.updateLine(line);
        return subwayRepository.findStationIdByName(stationToAdd.getName())
                .orElseThrow(() -> new NoSuchElementException("디버깅: 역이 추가되어야 하는데 안됐습니다"));
    }

    public void deleteStation(DeleteStationRequest deleteStationRequest) {
        Line line = getLine(deleteStationRequest.getLineName());
        Station stationToDelete = Station.from(deleteStationRequest.getStationName());
        line.deleteStation(stationToDelete);

        subwayRepository.updateLine(line);
    }

    private Line getLine(String lineNameInput) {
        LineName lineName = new LineName(lineNameInput);

        return subwayRepository.getLineByName(lineName);
    }

    public Long addNewLine(AddLineRequest addLineRequest) {
        LineName lineNameToAdd = new LineName(addLineRequest.getName());
        Station upstream = getStation(addLineRequest.getUpstreamName());
        Station downstream = getStation(addLineRequest.getDownstreamName());
        Section section = new Section(upstream, downstream, addLineRequest.getDistance());
        Line newLine = new Line(lineNameToAdd, section);
        Lines lines = subwayRepository.getLines();
        lines.addNewLine(newLine);

        return subwayRepository.addNewLine(newLine);
    }

    private Station getStation(String stationName) {
        Stations stations = subwayRepository.getStations();
        Station station = stations.getStationByName(stationName)
                .orElseGet(() -> createNewStation(stationName));
        return station;
    }

    private Station createNewStation(String stationName) {
        Station newStation = Station.from(stationName);
        subwayRepository.addStation(newStation);
        return newStation;
    }

    public LineResponse findLineById(Long id) {
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
}
