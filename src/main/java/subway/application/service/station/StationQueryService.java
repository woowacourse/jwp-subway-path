package subway.application.service.station;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.port.in.station.FindAllStationsUseCase;
import subway.application.port.in.station.FindStationByIdUseCase;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.application.port.out.station.LoadStationPort;
import subway.application.service.exception.NoSuchStationException;
import subway.application.service.mapper.StationMapper;
import subway.domain.station.Station;

@Service
@Transactional(readOnly = true)
public class StationQueryService implements FindStationByIdUseCase, FindAllStationsUseCase {

    private final LoadStationPort loadStationPort;

    public StationQueryService(final LoadStationPort loadStationPort) {
        this.loadStationPort = loadStationPort;
    }

    @Override
    public StationQueryResponse findStationById(final long stationId) {
        Station station = loadStationPort.findById(stationId)
                .orElseThrow(NoSuchStationException::new);
        return StationMapper.toResponse(station);
    }

    @Override
    public List<StationQueryResponse> findAllStations() {
        List<Station> stations = loadStationPort.findAll();
        return stations.stream()
                .map(StationMapper::toResponse)
                .collect(Collectors.toList());
    }
}
