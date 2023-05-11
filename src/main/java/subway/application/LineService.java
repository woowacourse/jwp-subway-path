package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.station.StationConnections;
import subway.dto.LineFindResponse;
import subway.entity.LineEntity;
import subway.entity.SectionStationJoinEntity;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public LineFindResponse findStationNamesByLineId(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        List<SectionStationJoinEntity> findSectionStationEntities = sectionDao.findSectionStationByLineId(lineId);
        StationConnections stationConnections = StationConnections.fromEntities(findSectionStationEntities);
        return new LineFindResponse(lineEntity.getName(), stationConnections.getSortedStationNames());
    }

    public List<LineFindResponse> findAllLineStationNames() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> findStationNamesByLineId(lineEntity.getId()))
                .collect(toList());
    }
}
