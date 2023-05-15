package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.LineRoute;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionRequest;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void addStations(final SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        List<Section> sections = sectionDao.findByLineId(lineId);

        // TODO Dao에서 Optional 반환해서 유효한 id인지 검증
        Station baseStation = stationDao.findById(sectionRequest.baseStationId());
        Station nextStation = stationDao.findById(sectionRequest.nextStationId());
        Integer addingDistance = sectionRequest.getSectionStations().getDistance();
        Direction direction = sectionRequest.getSectionDirection().getDirection();

        // TODO LineRoute가 꼭 line을 객체로 가져야 할까?
        LineRoute lineRoute = LineRoute.of(new Line(lineId), sections);
        lineRoute.add(baseStation, nextStation, new Distance(addingDistance), direction);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertAllByLineId(lineId, lineRoute.extractSections());
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        Station station = stationDao.findById(stationId);
        List<Section> sections = sectionDao.findByLineId(lineId);

        LineRoute lineRoute = LineRoute.of(new Line(lineId), sections);
        lineRoute.delete(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertAllByLineId(lineId, lineRoute.extractSections());
    }
}
