package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.InitialStationsAddRequest;
import subway.dto.SectionResponse;
import subway.exception.DomainException;
import subway.exception.ExceptionType;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    //// TODO: 2023/05/09 : 오버로딩 할거임 ㅋㅋ
    @Transactional
    public SectionResponse addSection(InitialStationsAddRequest initialStationsAddRequest) {
        Long firstStationId = initialStationsAddRequest.getFirstStationId();
        Long secondStationId = initialStationsAddRequest.getSecondStationId();
        Long lineId = initialStationsAddRequest.getLineId();
        Integer distanceValue = initialStationsAddRequest.getDistance();

        if (hasAnyStation(lineId)) {
            throw new DomainException(ExceptionType.LINE_HAS_STATION);
        }

        Station firstStation = stationDao.findById(firstStationId);
        Station secondStation = stationDao.findById(secondStationId);
        Line line = lineDao.findById(lineId);
        Distance distance = new Distance(distanceValue);

        Section section = new Section(firstStation, secondStation, line, distance);
        Long id = sectionDao.insert(section);

        return new SectionResponse(id, section);
    }

    private boolean hasAnyStation(Long lineId) {
        return !sectionDao.findAllSectionByLineId(lineId).isEmpty();
    }
}
