package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dao.SubwayMapRepository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayMap;
import subway.dto.CreateType;
import subway.dto.StationRequest;
import subway.dto.LineStationRequest;
import subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;
    private final LineService lineService;
    private final SubwayMapRepository subwayMapRepository;

    public StationService(final StationDao stationDao, final LineService lineService, final SubwayMapRepository subwayMapRepository) {
        this.stationDao = stationDao;
        this.lineService = lineService;
        this.subwayMapRepository = subwayMapRepository;
    }

    public List<StationResponse> findStationsByLineId(final Long lineId) {
        final SubwayMap subwayMap = subwayMapRepository.find();

        final Line line = lineService.findLineById(lineId);

        final List<Station> stations = subwayMap.stationsInLine(line);
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        final SubwayMap subwayMap = subwayMapRepository.find();
        Station station = subwayMap.addStation(new Station(stationRequest.getName()));
        subwayMapRepository.save(subwayMap);

        SubwayMap found = subwayMapRepository.find();
        return StationResponse.of(found.findStationByName(station.getName()));
    }

    public StationResponse findStationResponseById(final Long id) {
        return StationResponse.of(stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("없는 역입니다.")));
    }

    public List<StationResponse> findAllStationResponses() {
        final SubwayMap subwayMap = subwayMapRepository.find();

        List<Station> stations = subwayMap.getStationsOrderById();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(final Long id) {
        final SubwayMap subwayMap = subwayMapRepository.find();

        subwayMap.deleteStation(id);

        subwayMapRepository.save(subwayMap);
    }
}
