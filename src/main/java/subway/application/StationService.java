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

    public void saveStation(final List<StationRequest> stationRequests) {
        final CreateType type = CreateType.valueOf(stationRequests.get(0).getCreateType());
        final Line line = lineService.findLineByName(stationRequests.get(0).getLine());

        if (type == CreateType.INIT) {
            final StationRequest requestUp = stationRequests.get(0);
            final StationRequest requestDown = stationRequests.get(1);

            final SubwayMap subwayMap = new SubwayMap();
            final Station upStation = new Station(requestUp.getName());
            final Station downStation = new Station(requestDown.getName());
            final Section upToDown = new Section(requestUp.getNextDistance(), upStation, downStation, line);
            final Section downToUp = new Section(requestUp.getNextDistance(), downStation, upStation, line);

            subwayMap.addInitialStations(upToDown, downToUp);

            subwayMapRepository.save(subwayMap);
            return;
        }
        final StationRequest request = stationRequests.get(0);
        if (type == CreateType.UP) {
            final SubwayMap subwayMap = subwayMapRepository.find();

            final Station thisStation = new Station(request.getName());
            final Station nextStation = new Station(request.getNextStation());
            final Section thisToNext = new Section(request.getNextDistance(), thisStation, nextStation, line);

            subwayMap.addUpEndPoint(thisToNext);

            subwayMapRepository.save(subwayMap);
            return;
        }
        if (type == CreateType.DOWN) {
            final SubwayMap subwayMap = subwayMapRepository.find();

            final Station thisStation = new Station(request.getName());
            final Station prevStation = new Station(request.getPreviousStation());
            final Section thisToPrev = new Section(request.getPreviousDistance(), thisStation, prevStation, line);

            subwayMap.addDownEndPoint(thisToPrev);

            subwayMapRepository.save(subwayMap);
            return;
        }
        if (type == CreateType.MID) {
            validateRequestMid(request);

            final SubwayMap subwayMap = subwayMapRepository.find();

            final Station prevStation = new Station(request.getPreviousStation());
            final Station thisStation = new Station(request.getName());
            final Station nextStation = new Station(request.getNextStation());
            final Section thisToPrev = new Section(request.getPreviousDistance(), thisStation, prevStation, line);
            final Section thisToNext = new Section(request.getNextDistance(), thisStation, nextStation, line);

            subwayMap.addIntermediateStation(thisToPrev, thisToNext);

            subwayMapRepository.save(subwayMap);
            return;
        }
        throw new IllegalArgumentException("잘못된 타입입니다.");
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
        final SubwayMap subwayMap = subwayMapRepository.find();

        subwayMap.deleteStation(id);

        subwayMapRepository.save(subwayMap);
    }
}
