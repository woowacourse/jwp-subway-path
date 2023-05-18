package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.StationGraph;
import subway.dto.PathDto;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathDto findShortest(long startId, long endId) {
        List<Sections> allSections = sectionDao.findAll();
        Station start = stationDao.findById(startId);
        Station end = stationDao.findById(endId);

        StationGraph graph = new StationGraph(allSections);

        return new PathDto(graph.findShortestPath(start, end), graph.findDistance(start, end));
    }
}
