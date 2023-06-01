package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.path.PathStrategy;
import subway.domain.subway.Section;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final PathStrategy pathStrategy;

    public PathService(SectionDao sectionDao, PathStrategy pathStrategy) {
        this.sectionDao = sectionDao;
        this.pathStrategy = pathStrategy;
    }

    public PathResponse getDijkstraShortestPath(final PathRequest pathRequest) {
        List<Section> allSections = sectionDao.findAll().stream()
                .map(Section::of)
                .collect(Collectors.toList());

        Map.Entry<List<Long>, Integer> pathAndDistance = pathStrategy.getPathAndDistance(allSections, pathRequest.getStartStationId(), pathRequest.getTargetStationId());
        return new PathResponse(pathAndDistance.getKey(), pathAndDistance.getValue());
    }

}
