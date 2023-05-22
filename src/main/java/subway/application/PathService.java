package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.StationGraph;
import subway.dto.PathDto;

import java.util.ArrayList;
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

    public List<Section> findShortestPath(long startId, long endId) {
        List<Sections> allSections = sectionDao.findAll();
        Station start = stationDao.findById(startId);
        Station end = stationDao.findById(endId);
        StationGraph graph = new StationGraph(allSections);
        List<Station> shortestPath = graph.findShortestPath(start, end);

        List<Section> path = new ArrayList<>();
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            Section passingSection = sectionDao.findByStations(shortestPath.get(i), shortestPath.get(i + 1));
            path.add(passingSection);
        }
        return path;
    }
}
