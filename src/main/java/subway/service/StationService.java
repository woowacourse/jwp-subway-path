package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.dto.StationCreateRequest;
import subway.exception.DuplicateException;
import subway.exception.ErrorMessage;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long save(final StationCreateRequest stationCreateRequest) {
        StationEntity stationEntity = new StationEntity(stationCreateRequest.getName());

        if (stationDao.exists(stationEntity)) {
            throw new DuplicateException(ErrorMessage.DUPLICATE_NAME);
        }

        return stationDao.save(stationEntity);
    }
}
