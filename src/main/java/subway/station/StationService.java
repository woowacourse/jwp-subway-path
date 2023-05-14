package subway.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.Station;
import subway.station.dto.StationCreateDto;
import subway.station.persistence.StationDao;
import subway.station.persistence.StationEntity;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public Long create(final StationCreateDto stationCreateDto) {
        final Station station = new Station(stationCreateDto.getName());

        return stationDao.insert(new StationEntity(station.getName()));
    }

    public StationEntity findById(final Long stationId) {
        return stationDao.findById(stationId).orElseThrow(() -> new IllegalArgumentException("해당 이름의 역이 존재하지 않습니다."));
    }
}
