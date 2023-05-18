package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.PathDao;
import subway.dao.StationDao;
import subway.domain.FareStrategy;
import subway.domain.Shortest;
import subway.domain.Station;
import subway.domain.path.Paths;
import subway.dto.ShortestResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShortestService {

    private final PathDao pathDao;
    private final StationDao stationDao;
    private final FareStrategy fareStrategy;

    public ShortestService(final PathDao pathDao, StationDao stationDao, final FareStrategy fareStrategy) {
        this.pathDao = pathDao;
        this.stationDao = stationDao;
        this.fareStrategy = fareStrategy;
    }

    public ShortestResponse findShortest(final Long startId, final Long endId) {
        final Station start = stationDao.findById(startId);
        final Station end = stationDao.findById(endId);
        final List<Paths> allPaths = pathDao.findAll();

        final Shortest shortest = Shortest.from(allPaths);
        final Paths result = shortest.findShortest(start, end);

        return ShortestResponse.of(result, fareStrategy);
    }
}
