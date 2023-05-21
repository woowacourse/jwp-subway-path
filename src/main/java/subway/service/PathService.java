package subway.service;

import org.springframework.stereotype.Service;
import subway.controller.dto.request.ShortestPathRequest;
import subway.controller.dto.response.ShortestPathResponse;
import subway.entity.SectionDetailEntity;
import subway.entity.StationEntity;
import subway.repository.SectionDao;
import subway.repository.StationDao;

import java.util.List;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public ShortestPathResponse findShortestPath(final ShortestPathRequest request) {
        final List<SectionDetailEntity> allSectionEntities = sectionDao.findSectionDetail();
        final long startStationId = stationDao.findIdByName(request.getStartStationName());
        final long endStationId = stationDao.findIdByName(request.getEndStationName());
        return null;
    }
}
