package subway.application;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.exception.DomainException;
import subway.domain.exception.ExceptionType;
import subway.domain.fare.FareCalculator;
import subway.domain.fare.FareInfo;
import subway.domain.path.strategy.PathFindStrategy;
import subway.domain.path.PathInfo;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.PathFindingRequest;
import subway.dto.PathResponse;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final FareCalculator fareCalculator;
    private final PathFindStrategy pathFindStrategy;

    public PathService(
        StationDao stationDao,
        LineDao lineDao,
        SectionDao sectionDao,
        FareCalculator fareCalculator,
        PathFindStrategy pathFindStrategy) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.fareCalculator = fareCalculator;
        this.pathFindStrategy = pathFindStrategy;
    }

    public PathResponse findPathInfo(PathFindingRequest pathFindingRequest) {
        PathInfo pathInfo = getPathAndTotalDistance(pathFindingRequest);
        Map<Long, Station> idsToStations = getIdsToStations(pathInfo);

        List<Station> path = pathInfo.getPath().stream()
            .map(idsToStations::get)
            .collect(Collectors.toUnmodifiableList());

        int distance = pathInfo.getDistance();
        Set<Line> lines = getLines(pathInfo);
        int age = pathFindingRequest.getAge();
        int fee = fareCalculator.calculate(new FareInfo(distance, lines, age));

        return PathResponse.of(distance, path, fee);
    }

    private PathInfo getPathAndTotalDistance(PathFindingRequest pathFindingRequest) {
        List<Section> allSections = sectionDao.findAll();

        Long departureId = pathFindingRequest.getDepartureId();
        Long destinationId = pathFindingRequest.getDestinationId();


        return pathFindStrategy.findPathInfo(departureId, destinationId, allSections);
    }

    private Map<Long, Station> getIdsToStations(PathInfo pathInfo) {
        List<Station> stations = stationDao.findStationByList(pathInfo.getPath());
        return stations.stream()
            .collect(Collectors.toMap(Station::getId, Function.identity()));
    }

    private Set<Line> getLines(PathInfo pathInfo) {
        List<Line> allLines = lineDao.findAll();
        Set<Long> lineIds = pathInfo.getLineIds();

        return lineIds.stream()
            .map(id -> allLines.stream()
                .filter(line -> Objects.equals(line.getId(), id))
                .findFirst()
                .orElseThrow(() -> new DomainException(ExceptionType.NO_LINE)))
            .collect(Collectors.toSet());
    }
}
