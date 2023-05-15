package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;
import subway.entity.SectionEntity;
import subway.entity.SectionWithStationNameEntity;
import subway.exception.SectionException;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long addStation(Long lineId, LineStationRequest lineStationRequest) {
        Line line = lineDao.findById(lineId).toLine();
        Station preStation = stationDao.findById(lineStationRequest.getPreStationId()).toStation();
        Station station = stationDao.findById(lineStationRequest.getStationId()).toStation();

        Section section = new Section(line, preStation, station, lineStationRequest.getDistance());
        Sections currentLineSections = getCurrentLineSections(lineId);
        Section modified = currentLineSections.add(section);

        if (modified != null) {
            Long preStationId = (long) stationDao.findByName(modified.getPreStation().getName());
            Long postStationId = (long) stationDao.findByName(modified.getStation().getName());
            SectionEntity sectionEntity = new SectionEntity(lineId, preStationId, postStationId, modified.getDistance());
            if (preStationId.equals(lineStationRequest.getStationId())) {
                sectionDao.updatePreStation(sectionEntity);
            }
            if (postStationId.equals(lineStationRequest.getPreStationId())) {
                sectionDao.updateStation(sectionEntity);
            }
        }

        SectionEntity sectionEntity = new SectionEntity(lineId,
                lineStationRequest.getPreStationId(), lineStationRequest.getStationId(), lineStationRequest.getDistance());
        return sectionDao.save(sectionEntity);
    }

    public void removeStation(Long lineId, Long stationId) {
        Sections currentLineSections = getCurrentLineSections(lineId);
        Station station = stationDao.findById(stationId).toStation();

        if (!currentLineSections.isExistStation(station)) {
            throw new SectionException("노선에 없는 역은 삭제할 수 없습니다");
        }

        Section modified = currentLineSections.remove(station);
        sectionDao.remove(stationId);

        if (modified != null) {
            Long preStationId = (long) stationDao.findByName(modified.getPreStation().getName());
            Long postStationId = (long) stationDao.findByName(modified.getStation().getName());
            sectionDao.save(new SectionEntity(lineId,
                    preStationId, postStationId, modified.getDistance()));
        }
    }

    public LineStationResponse findByLineId(Long lineId) {
        Sections currentSections = getCurrentLineSections(lineId);

        List<Station> stations = SortedStations.from(currentSections).getStations();
        return new LineStationResponse(lineId, stations);
    }

    private Sections getCurrentLineSections(Long lineId) {
        Line line = lineDao.findById(lineId).toLine();
        List<SectionWithStationNameEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        List<Section> sections = new ArrayList<>();
        for (SectionWithStationNameEntity entity : sectionEntities) {
            sections.add(new Section(line, new Station(entity.getPreStationName()),
                    new Station(entity.getStationName()), entity.getDistance()));
        }
        return new Sections(sections);
    }
}
