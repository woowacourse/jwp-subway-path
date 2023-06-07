package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.fee.FeeStrategy;
import subway.domain.path.PathStrategy;
import subway.domain.subway.Distance;
import subway.domain.subway.Section;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionDao sectionDao;
    private final PathStrategy pathStrategy;
    private final FeeStrategy feeStrategy;

    public PathService(SectionDao sectionDao, PathStrategy pathStrategy, FeeStrategy feeStrategy) {
        this.sectionDao = sectionDao;
        this.pathStrategy = pathStrategy;
        this.feeStrategy = feeStrategy;
    }

    public PathResponse getPath(final PathRequest pathRequest) {
        List<Section> allSections = sectionDao.findAll();

        Map.Entry<List<Long>, Distance> pathAndDistance = pathStrategy.getPathAndDistance(allSections, pathRequest.getStartStationId(), pathRequest.getTargetStationId());
        List<Long> path = pathAndDistance.getKey();
        Distance distance = pathAndDistance.getValue();
        return new PathResponse(path, distance.getDistance(), feeStrategy.calculateFee(distance));
    }

}
