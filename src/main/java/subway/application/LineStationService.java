package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Station;
import subway.dto.response.LineStationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineStationService {

    private final LineService lineService;
    private final StationService stationService;

    public LineStationService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public void addInitStations(final Long id, final Long upStationId, final Long downStationId, final int distance) {
        final Line line = lineService.findById(id);
        final Station upStation = stationService.findById(upStationId);
        final Station downStation = stationService.findById(downStationId);

        line.addInitStations(upStation, downStation, distance);

        lineService.save(line);
    }

    public void addUpEndpoint(final Long id, final Long stationId, final int distance) {
        final Line line = lineService.findById(id);
        final Station station = stationService.findById(stationId);

        line.addUpEndpoint(station, distance);

        lineService.save(line);
    }

    public void addDownEndpoint(final Long id, final Long stationId, final int distance) {
        final Line line = lineService.findById(id);
        final Station station = stationService.findById(stationId);

        line.addDownEndpoint(station, distance);

        lineService.save(line);
    }

    public void addIntermediate(final Long id, final Long stationId, final Long prevStationId, final int distance) {
        final Line line = lineService.findById(id);
        final Station station = stationService.findById(stationId);
        final Station prevStation = stationService.findById(prevStationId);

        line.addIntermediate(station, prevStation, distance);

        lineService.save(line);
    }

    public void deleteStationInLine(final Long id, final Long stationId) {
        final Line line = lineService.findById(id);
        final Station station = stationService.findById(stationId);

        line.deleteSections(station);

        lineService.save(line);
    }

    public void deleteStation(final Long stationId) {
        final Lines lines = lineService.findAll();
        final Station station = stationService.findById(stationId);

        lines.deleteStation(station);

        stationService.deleteStationById(stationId);
    }

    @Transactional(readOnly = true)
    public List<LineStationResponse> findAll() {
        return lineService.findAll().getLines().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineStationResponse findByLineId(final Long id) {
        final Line line = lineService.findById(id);
        return mapToResponse(line);
    }

    private LineStationResponse mapToResponse(final Line line) {
        final List<String> stations = stationsToString(line.getAllStations());
        final List<Integer> getDistances = line.getAllDistances();
        return new LineStationResponse(stations, getDistances);
    }

    private List<String> stationsToString(final List<Station> stations) {
        return stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }
}
