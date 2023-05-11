package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.SubwayGraphs;

@Service
public class LineFindService {

    private final SubwayGraphs subwayGraphs;

    private final LineDao lineDao;
    private final StationDao stationDao;

    public LineFindService(final SubwayGraphs subwayGraphs, final LineDao lineDao, final StationDao stationDao) {
        this.subwayGraphs = subwayGraphs;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }
}
