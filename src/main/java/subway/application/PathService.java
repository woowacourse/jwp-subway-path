package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.GraphPath;
import subway.domain.Sections;
import subway.dto.ShortPathResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final GraphPath graphPath;

    public PathService(SectionDao sectionDao, GraphPath graphPath) {
        this.sectionDao = sectionDao;
        this.graphPath = graphPath;
    }

    public ShortPathResponse findShortestPathInfo(String startStationName, String endStationName) {
        Sections allSectionInfo = sectionDao.findAllSectionInfo();
        List<String> shortestPath = graphPath.getShortestPath(allSectionInfo, startStationName, endStationName);
        Distance shortestDistance = graphPath.getShortestDistance(allSectionInfo, startStationName, endStationName);
        Fare fare = Fare.of(shortestDistance);
        return new ShortPathResponse(shortestPath, shortestDistance, fare);
    }

}
