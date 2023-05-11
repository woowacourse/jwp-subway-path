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
import subway.dto.StationResponse;

import java.util.Collections;
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

    public List<StationResponse> saveStation(final List<StationRequest> stationRequests) {
        final CreateType type = stationRequests.get(0).getType();
        final Line line = lineService.findLineByName(stationRequests.get(0).getLine());

        if (type == CreateType.INIT) {
            final StationRequest requestUp = stationRequests.get(0);
            final StationRequest requestDown = stationRequests.get(1);

            SubwayMap subwayMap = subwayMapRepository.find();
            Station upStation = new Station(requestUp.getName());
            Station downStation = new Station(requestDown.getName());
            Section upToDown = new Section(requestUp.getNextDistance(), upStation, downStation, line);

            subwayMap.addInitialStations(upToDown, upToDown.getReverse());

            subwayMapRepository.save(subwayMap);
            return List.of(StationResponse.of(upStation), StationResponse.of(downStation));
        }
        final StationRequest request = stationRequests.get(0);
        if (type == CreateType.UP) {
            SubwayMap subwayMap = subwayMapRepository.find();

            Station thisStation = new Station(request.getName());
            Station nextStation = new Station(request.getNextStation());
            Section thisToNext = new Section(request.getNextDistance(), thisStation, nextStation, line);

            subwayMap.addEndPointStation(thisToNext);

            subwayMapRepository.save(subwayMap);
            return List.of(StationResponse.of(thisStation));
        }
        if (type == CreateType.DOWN) {
            SubwayMap subwayMap = subwayMapRepository.find();

            Station thisStation = new Station(request.getName());
            Station prevStation = new Station(request.getPreviousStation());
            Section thisToPrev = new Section(request.getPreviousDistance(), thisStation, prevStation, line);

            subwayMap.addEndPointStation(thisToPrev);

            subwayMapRepository.save(subwayMap);
            return List.of(StationResponse.of(thisStation));
        }
        if (type == CreateType.MID) {
            validateRequestMid(request);

            SubwayMap subwayMap = subwayMapRepository.find();

            Station prevStation = new Station(request.getPreviousStation());
            Station thisStation = new Station(request.getName());
            Station nextStation = new Station(request.getNextStation());
            Section thisToPrev = new Section(request.getPreviousDistance(), thisStation, prevStation, line);
            Section thisToNext = new Section(request.getNextDistance(), thisStation, nextStation, line);

            subwayMap.addIntermediateStation(thisToPrev, thisToNext);

            subwayMapRepository.save(subwayMap);

            return List.of(StationResponse.of(thisStation));
        }

        return Collections.emptyList();
    }

    private void validateRequestMid(final StationRequest request) {
        if (request.getNextStation() == null || request.getNextDistance() == null) {
            throw new IllegalArgumentException("잘못된 중간역을 등록했습니다: 다음 역 오류");
        }
        if (request.getPreviousStation() == null || request.getPreviousDistance() == null) {
            throw new IllegalArgumentException("잘못된 중간역을 등록했습니다: 이전 역 오류");
        }
    }

    public StationResponse findStationResponseById(final Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    public List<StationResponse> findAllStationResponses() {
        final List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
