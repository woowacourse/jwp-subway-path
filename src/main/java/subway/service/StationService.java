package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.dto.StationCreateRequest;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long save(StationCreateRequest stationCreateRequest) {
        duplicateLineName(stationCreateRequest.getName());
        return stationDao.save(new StationEntity(stationCreateRequest.getName()));
    }

    private void duplicateLineName(String name) {
        if (stationDao.isExisted(name)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }
    }
}
