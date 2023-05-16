package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.response.LineStationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineStationService {

    private final LineService lineService;
    private final StationService stationService;

    public LineStationService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public void addInitStations(final Long id, final Long upStationId, final Long downStationId, final int distance) {
        Line line = lineService.findById(id);
        Station upStation = stationService.findById(upStationId);
        Station downStation = stationService.findById(downStationId);
        line.addInitStations(upStation, downStation, distance);
        lineService.save(line);
    }

    public void addUpEndpoint(final Long id, final Long stationId, final int distance) {
        Line line = lineService.findById(id);
        Station station = stationService.findById(stationId);
        line.addUpEndpoint(station, distance);
        lineService.save(line);
    }

    public void addDownEndpoint(final Long id, final Long stationId, final int distance) {
        Line line = lineService.findById(id);
        Station station = stationService.findById(stationId);
        line.addDownEndpoint(station, distance);
        lineService.save(line);
    }

    public void addIntermediate(final Long id, final Long stationId, final Long prevStationId, final int distance) {
        Line line = lineService.findById(id);
        Station station = stationService.findById(stationId);
        Station prevStation = stationService.findById(prevStationId);
        line.addIntermediate(station, prevStation, distance);
        lineService.save(line);
    }

    public void deleteStationById(final Long id, final Long stationId) {
        Line line = lineService.findById(id);
        Station station = stationService.findById(stationId);
        line.deleteSections(station);
        lineService.save(line);
    }

    public void deleteStation(final Long stationId) {
        List<Line> lines = lineService.findAll();
        lines.forEach(line -> deleteStationById(line.getId(), stationId));
        stationService.deleteStationById(stationId);
    }

    public List<LineStationResponse> findAll() {
        List<Line> lines = lineService.findAll();

        return lines.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public LineStationResponse findByLineId(Long id) {
        Line line = lineService.findById(id);

        return mapToResponse(line);
    }

    private LineStationResponse mapToResponse(Line line) {
        List<String> stations = stationsToString(line.getAllStations());
        List<Integer> getDistances = line.getAllDistances();
        return new LineStationResponse(stations, getDistances);
    }

    private List<String> stationsToString(List<Station> stations) {
        return stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }
}
