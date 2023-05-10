package subway.service;

import java.util.ArrayList;
import java.util.HashMap;
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
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);

        HashMap<Long, Long> stationsIds = new HashMap<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            Long sectionStartStationId = sectionEntity.getStartStationId();
            Long sectionEndStationId = sectionEntity.getEndStationId();
            stationsIds.put(sectionStartStationId, sectionEndStationId);
        }

        Long currentKey = findFirstStationId(sectionEntities);

        List<Long> sortedStationIds = new ArrayList<>();
        while (stationsIds.containsKey(currentKey)) {
            sortedStationIds.add(currentKey);
            currentKey = stationsIds.get(currentKey);
        }

        return new LineResponse(lineEntity.getId(), lineEntity.getName(), makeStationResponses(sortedStationIds));
    }

    private Long findFirstStationId(final List<SectionEntity> sectionEntities) {
        List<Long> startStationIds = new ArrayList<>();
        List<Long> endStationIds = new ArrayList<>();

        for (SectionEntity sectionEntity : sectionEntities) {
            Long sectionStartStationId = sectionEntity.getStartStationId();
            Long sectionEndStationId = sectionEntity.getEndStationId();
            startStationIds.add(sectionStartStationId);
            endStationIds.add(sectionEndStationId);
        }
        startStationIds.removeAll(endStationIds);
        return startStationIds.get(0);
    }

    private List<StationResponse> makeStationResponses(final List<Long> sortedStationIds) {
        return sortedStationIds.stream()
                .map(stationDao::findById)
                .map(stationEntity -> new StationResponse(stationEntity.getName()))
                .collect(Collectors.toList());
    }

    public List<LineResponse> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                .map(lineEntity -> findById(lineEntity.getId()))
                .collect(Collectors.toList());
    }
}
