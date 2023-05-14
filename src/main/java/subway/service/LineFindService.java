package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.DbLineDao;
import subway.dao.StationDao;
import subway.domain.SubwayGraphs;

@Service
public class LineFindService {

    private final SubwayGraphs subwayGraphs;

    private final DbLineDao dbLineDao;
    private final StationDao stationDao;

    public LineFindService(final SubwayGraphs subwayGraphs, final DbLineDao dblineDao, final StationDao stationDao) {
        this.subwayGraphs = subwayGraphs;
        this.dbLineDao = dblineDao;
        this.stationDao = stationDao;
    }
}
