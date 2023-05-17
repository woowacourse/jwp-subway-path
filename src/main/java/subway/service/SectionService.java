package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.SectionWithStationNameEntity;
import subway.entity.StationEntity;
import subway.exception.SectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
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
        Line line = findLineById(lineId);

        Station preStation = findStationById(lineStationRequest.getPreStationId());
        Station station = findStationById(lineStationRequest.getStationId());

        Section section = new Section(line, preStation, station, lineStationRequest.getDistance());
        Sections currentLineSections = getCurrentLineSections(lineId);
        Section modified = currentLineSections.add(section);

        if (modified != null) {
            Long preStationId = stationDao.findIdByName(modified.getPreStation().getName());
            Long postStationId = stationDao.findIdByName(modified.getStation().getName());
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

    private Line findLineById(Long lineId) {
        Optional<LineEntity> lineEntity = lineDao.findById(lineId);
        if (lineEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다");
        }
        Line line = lineEntity.get().toLine();
        return line;
    }

    public void removeStation(Long lineId, Long stationId) {
        Sections currentLineSections = getCurrentLineSections(lineId);
        Station station = findStationById(stationId);

        if (!currentLineSections.isExistStation(station)) {
            throw new SectionException("노선에 없는 역은 삭제할 수 없습니다");
        }

        Section modified = currentLineSections.remove(station);
        sectionDao.remove(stationId);

        if (modified != null) {
            Long preStationId = stationDao.findIdByName(modified.getPreStation().getName());
            Long postStationId = stationDao.findIdByName(modified.getStation().getName());
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
        Optional<LineEntity> lineEntity = lineDao.findById(lineId);
        if (lineEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다");
        }
        Line line = lineEntity.get().toLine();
        List<SectionWithStationNameEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        List<Section> sections = new ArrayList<>();
        for (SectionWithStationNameEntity entity : sectionEntities) {
            sections.add(new Section(line, new Station(entity.getPreStationName()),
                    new Station(entity.getStationName()), entity.getDistance()));
        }
        return new Sections(sections);
    }

    private Station findStationById(Long id) {
        Optional<StationEntity> stationEntity = stationDao.findById(id);
        if (stationEntity.isPresent()) {
            return stationEntity.get().toStation();
        }
        throw new IllegalArgumentException("존재하지 않는 역입니다");
    }
}
