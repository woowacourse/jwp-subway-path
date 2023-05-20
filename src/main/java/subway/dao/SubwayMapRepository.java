package subway.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Station;
import subway.exception.custom.StationNotExistException;

@Repository
public class SubwayMapRepository extends SectionRepository {

    public SubwayMapRepository(final LineDao lineDao, final StationDao stationDao,
        final SectionDao sectionDao) {
        super(lineDao, stationDao, sectionDao);
    }

    public List<Line> findAllLines() {
        return lineDao.findAll();
    }

    public Station findStationByName(final String stationName) {
        return stationDao.findByName(stationName)
            .orElseThrow(() -> new StationNotExistException("해당 역이 지하철 노선도에 존재하지 않습니다. ( " + stationName + " )"));
    }
}
