package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
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

    public LineService(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long save(final LineRequest lineRequest) {
        if (isExisted(lineRequest)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }
        return lineDao.save(new LineEntity(lineRequest.getName()));
    }

    @Transactional(readOnly = true)
    public boolean isExisted(final LineRequest lineRequest) {
        try {
            lineDao.findByName(lineRequest.getName());
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public LineResponse findById(final Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        Line line = Line.of(lineEntity.getName(), makeSections(id));
        List<StationResponse> stationsResponse = line.getStations().stream()
                .map(station -> new StationResponse(station.getName()))
                .collect(Collectors.toList());

        return new LineResponse(lineEntity.getId(), lineEntity.getName(), stationsResponse);
    }

    private List<Section> makeSections(final Long id) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);

        return sectionEntities.stream()
                .map(entity -> new Section(
                        new Station(stationDao.findById(entity.getUpStationId()).getName()),
                        new Station(stationDao.findById(entity.getDownStationId()).getName()),
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
