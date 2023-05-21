package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Station;
import subway.dto.response.LineStationResponse;
import subway.dto.response.StationResponse;
import subway.ui.EndpointType;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineStationService {

    private final LineService lineService;
    private final StationService stationService;
    private final PathService pathService;

    public LineStationService(final LineService lineService, final StationService stationService, final PathService pathService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathService = pathService;
    }

    public void addInitStations(final Long id, final Long upStationId, final Long downStationId, final int distance) {
        final Line line = lineService.findById(id);
        final Station upStation = stationService.findById(upStationId);
        final Station downStation = stationService.findById(downStationId);

        line.addInitStations(upStation, downStation, distance);

        lineService.save(line);
        updatePath();
    }

    private void updatePath() {
        final Lines lines = lineService.findAll();
        pathService.update(lines);
    }

    public void addEndpoint(final EndpointType endpointType, final Long id, final Long stationId, final int distance) {
        if (EndpointType.UP == endpointType) {
            addUpEndpoint(id, stationId, distance);
            return;
        }
        if (EndpointType.DOWN == endpointType) {
            addDownEndpoint(id, stationId, distance);
        }
    }

    private void addUpEndpoint(final Long id, final Long stationId, final int distance) {
        final Line line = lineService.findById(id);
        final Station station = stationService.findById(stationId);

        line.addUpEndpoint(station, distance);

        lineService.save(line);
        updatePath();
    }

    private void addDownEndpoint(final Long id, final Long stationId, final int distance) {
        final Line line = lineService.findById(id);
        final Station station = stationService.findById(stationId);

        line.addDownEndpoint(station, distance);

        lineService.save(line);
        updatePath();
    }

    public void addIntermediate(final Long id, final Long stationId, final Long prevStationId, final int distance) {
        final Line line = lineService.findById(id);
        final Station station = stationService.findById(stationId);
        final Station prevStation = stationService.findById(prevStationId);

        line.addIntermediate(station, prevStation, distance);

        lineService.save(line);
        updatePath();
    }

    public void deleteStationInLine(final Long id, final Long stationId) {
        final Line line = lineService.findById(id);
        final Station station = stationService.findById(stationId);

        line.deleteSections(station);

        lineService.save(line);
        updatePath();
    }

    public void deleteStation(final Long stationId) {
        final Lines lines = lineService.findAll();
        final Station station = stationService.findById(stationId);

        lines.deleteStation(station);

        stationService.deleteStationById(stationId);
        updatePath();
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
        final List<StationResponse> stationResponses = mapToStationResponses(line.getAllStations());
        final List<Integer> getDistances = line.getAllDistances();
        return new LineStationResponse(stationResponses, getDistances);
    }

    private List<StationResponse> mapToStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
