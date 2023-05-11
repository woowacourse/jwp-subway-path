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
        Station stationToAdd = Station.from(addStationRequest.getAddStationName());
        validateStationExist(stationToAdd);

        LineNames lineNames = subwayRepository.getLineNames();
        String inputLineName = addStationRequest.getLineName();
        lineNames.validateLineExist(inputLineName);

        Line line = subwayRepository.getLineByName(inputLineName);
        Station upstream = Station.from(addStationRequest.getUpstreamName());
        Station downstream = Station.from(addStationRequest.getDownstreamName());
        line.addStation(stationToAdd, upstream, downstream, addStationRequest.getDistanceToUpstream());
        subwayRepository.updateLine(line);

        return subwayRepository.findStationIdByName(stationToAdd.getName())
                .orElseThrow(() -> new NoSuchElementException("디버깅: 역이 추가되어야 하는데 안됐습니다"));
    }

    public void deleteStation(DeleteStationRequest deleteStationRequest) {
        Station stationToDelete = Station.from(deleteStationRequest.getStationName());
        validateStationExist(stationToDelete);

        LineNames lineNames = subwayRepository.getLineNames();
        lineNames.validateLineExist(deleteStationRequest.getLineName());

        Line line = subwayRepository.getLineByName(deleteStationRequest.getLineName());
        line.deleteStation(stationToDelete);
        subwayRepository.updateLine(line);
    }

    private void validateStationExist(Station stationToAdd) {
        Stations stations = subwayRepository.getStations();
        if (!stations.contains(stationToAdd)) {
            subwayRepository.addStation(stationToAdd);
        }
    }

    public Long addLine(AddLineRequest addLineRequest) {
        LineNames lineNames = subwayRepository.getLineNames();
        lineNames.validateLineNotExist(addLineRequest.getName());

        Station upstream = Station.from(addLineRequest.getUpstreamName());
        Station downstream = Station.from(addLineRequest.getDownstreamName());
        Section section = new Section(upstream, downstream, addLineRequest.getDistance());
        Line line = new Line(addLineRequest.getName(), List.of(section));

        registerStationIfNotExist(upstream, downstream);
        subwayRepository.addLine(addLineRequest.getName());

        return subwayRepository.updateLine(line);
    }

    private void registerStationIfNotExist(Station upstream, Station downstream) {
        Stations stations = subwayRepository.getStations();
        if (!stations.contains(upstream)) {
            subwayRepository.addStation(upstream);
        }
        if (!stations.contains(downstream)) {
            subwayRepository.addStation(downstream);
        }
    }

    public LineResponse findLineById(Long id) {
        Line line = subwayRepository.getLineById(id).orElseThrow(() -> new LineNotFoundException("조회하고자 하는 노선이 없습니다"));
        return toLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return subwayRepository.getLines()
                .stream()
                .map(SubwayService::toLineResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    private static LineResponse toLineResponse(Line line) {
        return new LineResponse(line.getStationNamesInOrder(), line.getName());
    }
}