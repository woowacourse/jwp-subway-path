package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.StationRequest;
import subway.dto.StationSaveResponse;

@Service
@Transactional
public class StationService {

    // TODO: StationService 구현
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public StationSaveResponse saveStation(StationRequest stationRequest) {
        return null;
    }

    public void deleteStationById(Long id) {

    }
}