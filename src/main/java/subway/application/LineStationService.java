package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.response.StationResponse;

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

    public StationResponse findByLineId(Long id) {
        return null;
    }
}
