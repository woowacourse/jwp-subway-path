package subway.repository;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;

public class StationRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationRepository(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

}
