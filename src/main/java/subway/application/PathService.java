package subway.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.fee.FeeStrategy;
import subway.domain.path.PathFindStrategy;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.PathFindingRequest;
import subway.dto.PathResponse;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final FeeStrategy feeStrategy;
    private final PathFindStrategy pathFindStrategy;

    public PathService(
        StationDao stationDao,
        SectionDao sectionDao,
        FeeStrategy feeStrategy,
        PathFindStrategy pathFindStrategy) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.feeStrategy = feeStrategy;
        this.pathFindStrategy = pathFindStrategy;
    }

    public PathResponse findPathInfo(PathFindingRequest pathFindingRequest) {
        Map.Entry<List<Long>, Integer> pathAndTotalDistance = getPathAndTotalDistance(pathFindingRequest);
        Map<Long, Station> idsToStations = getIdsToStations(pathAndTotalDistance);

        List<Station> path = pathAndTotalDistance.getKey().stream()
            .map(idsToStations::get)
            .collect(Collectors.toUnmodifiableList());

        int distance = pathAndTotalDistance.getValue();

        int fee = feeStrategy.calculate(distance);

        return PathResponse.of(distance, path, fee);
    }

    private Map.Entry<List<Long>, Integer> getPathAndTotalDistance(PathFindingRequest pathFindingRequest) {
        List<Section> allSections = sectionDao.findAll();

        Long departureId = pathFindingRequest.getDepartureId();
        Long destinationId = pathFindingRequest.getDestinationId();

        return pathFindStrategy.findPathAndTotalDistance(departureId, destinationId, allSections);
    }

    private Map<Long, Station> getIdsToStations(Map.Entry<List<Long>, Integer> pathAndTotalDistance) {
        List<Station> stations = stationDao.findStationByList(pathAndTotalDistance.getKey());
        return stations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));
    }
}
