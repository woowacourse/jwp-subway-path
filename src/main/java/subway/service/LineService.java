package subway.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long save(LineRequest lineRequest) {
        duplicateLineName(lineRequest.getName());
        return lineDao.save(new LineEntity(lineRequest.getName()));
    }

    private void duplicateLineName(String name) {
        if (lineDao.isExisted(name)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        Line line = Line.of(lineEntity.getName(), makeSections(id));
        List<StationResponse> stationsResponse = line.getStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new LineResponse(id, lineEntity.getName(), stationsResponse);
    }

    private List<Section> makeSections(Long id) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        Map<Long, String> stationEntities = stationDao.findAll().stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        StationEntity::getName)
                );

        return sectionEntities.stream()
                .map(entity -> new Section(
                        new Station(entity.getUpStationId(), stationEntities.get(entity.getUpStationId())),
                        new Station(entity.getDownStationId(), stationEntities.get(entity.getDownStationId())),
                        entity.getDistance()
                )).collect(Collectors.toList());
    }

    public List<LineResponse> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                .map(lineEntity -> findById(lineEntity.getId()))
                .collect(Collectors.toList());
    }
}
