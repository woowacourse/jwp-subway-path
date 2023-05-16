package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.LineRoute;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.exception.RequestDataNotFoundException;
import subway.dto.SectionRequest;

@Service
public class SectionService {

    private static final String EXCEPTION_MESSAGE_STATION_ID_NOT_FOUND = "해당 Id를 가진 역 정보가 존재하지 않습니다.";
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void addStations(final SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        List<Section> sections = sectionDao.findByLineId(lineId);

        Station baseStation = stationDao.findById(sectionRequest.baseStationId())
                .orElseThrow(() -> new RequestDataNotFoundException("기준 역: " + EXCEPTION_MESSAGE_STATION_ID_NOT_FOUND));
        Station nextStation = stationDao.findById(sectionRequest.nextStationId())
                .orElseThrow(() -> new RequestDataNotFoundException("다음 역: " + EXCEPTION_MESSAGE_STATION_ID_NOT_FOUND));

        Distance addingDistance = new Distance(sectionRequest.getSectionStations().getDistance());
        Direction direction = sectionRequest.getDirection();

        LineRoute lineRoute = LineRoute.of(sections);
        lineRoute.add(baseStation, nextStation, addingDistance, direction);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertAllByLineId(lineId, lineRoute.extractSections());
    }

    @Transactional
    public void deleteStation(final Long lineId, final Long stationId) {
        List<Section> sections = sectionDao.findByLineId(lineId);

        Station station = stationDao.findById(stationId)
                .orElseThrow(() -> new RequestDataNotFoundException(EXCEPTION_MESSAGE_STATION_ID_NOT_FOUND));

        LineRoute lineRoute = LineRoute.of(sections);
        lineRoute.delete(station);

        sectionDao.deleteByLineId(lineId);
        sectionDao.insertAllByLineId(lineId, lineRoute.extractSections());
    }
}
