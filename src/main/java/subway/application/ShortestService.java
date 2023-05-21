package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Shortest;
import subway.domain.Station;
import subway.domain.fare.FareCalculator;
import subway.domain.path.PathEdgeProxy;
import subway.dto.ShortestResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShortestService {

    private final StationDao stationDao;
    private final LineService lineService;
    private final FareCalculator fareCalculator;

    public ShortestService(StationDao stationDao, final LineService lineService, final FareCalculator fareCalculator) {
        this.stationDao = stationDao;
        this.lineService = lineService;
        this.fareCalculator = fareCalculator;
    }

    public ShortestResponse findShortest(final Long startId, final Long endId) {
        final Station start = stationDao.findById(startId);
        final Station end = stationDao.findById(endId);

        final List<Line> lines = lineService.findAllLines();
        final Shortest shortest = Shortest.from(lines);

        final List<PathEdgeProxy> result = shortest.findShortest(start, end);
        return ShortestResponse.of(result, fareCalculator.of(result));
    }
}
