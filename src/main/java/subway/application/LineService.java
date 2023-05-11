package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.station.StationConnections;
import subway.dto.LineFindResponse;
import subway.entity.LineEntity;
import subway.entity.SectionStationJoinEntity;

import java.util.List;

@Service
public class LineService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public List<LineFindResponse> findAllLineStationNames() {
        return null;
    }

    public LineFindResponse findStationNamesByLineId(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        List<SectionStationJoinEntity> findSectionStationEntities = sectionDao.findSectionStationByLineId(lineId);
        StationConnections stationConnections = StationConnections.fromEntities(findSectionStationEntities);
        return new LineFindResponse(lineEntity.getName(), stationConnections.getSortedStationNames());
    }
}
