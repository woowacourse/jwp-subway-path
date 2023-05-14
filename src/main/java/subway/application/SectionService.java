package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.LineStationRequest;
import subway.dto.LineStationResponse;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long addStation(Long lineId, LineStationRequest lineStationRequest) {
        Section section = new Section(lineId, lineStationRequest.getPreStationId(), lineStationRequest.getStationId(), lineStationRequest.getDistance());
        Sections currentLineSections = getCurrentLineSections(lineId);
        Section modified = currentLineSections.add(section);

        if (modified != null) {
            sectionDao.updatePre(modified);
        }

        SectionEntity sectionEntity = new SectionEntity(lineId,
                lineStationRequest.getPreStationId(), lineStationRequest.getStationId(), lineStationRequest.getDistance());
        return sectionDao.save(sectionEntity);
    }

    private Sections getCurrentLineSections(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        List<Section> sections = new ArrayList<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            sections.add(new Section(lineId, sectionEntity.getPreStationId(),
                    sectionEntity.getStationId(), sectionEntity.getDistance()));
        }
        return new Sections(sections);
    }

    public void removeStation(Long lineId, Long stationId) {
        Sections currentLineSections = getCurrentLineSections(lineId);

        if (!currentLineSections.isExistStation(stationId)) {
            throw new RuntimeException("노선에 없는 역은 삭제할 수 없습니다");
        }

        if (currentLineSections.getSections().size() == 1) {
            currentLineSections.removeWhenOnlyTwoStationsExist(stationId);
            sectionDao.remove(stationId);
            return;
        }

        Section modified = currentLineSections.remove(stationId);

        sectionDao.remove(stationId);
        if (modified != null) {
            sectionDao.save(new SectionEntity(modified.getLineId(),
                    modified.getPreStationId(), modified.getStationId(), modified.getDistance()));
        }
    }

    public LineStationResponse findByLineId(Long lineId) {
        Sections currentSections = getCurrentLineSections(lineId);
        List<PairId> pairStationIds = currentSections.getPairStationIds();
        List<PairStation> pairStations = new ArrayList<>();
        for (PairId pairId : pairStationIds) {
            StationEntity preStationEntity = stationDao.findById(pairId.getPreStationId());
            StationEntity stationEntity = stationDao.findById(pairId.getStationId());

            Station preStation = new Station(preStationEntity.getName());
            Station station = new Station(stationEntity.getName());
            pairStations.add(new PairStation(preStation, station));
        }
        List<Station> stations = StationConverter.getStations(pairStations);
        return new LineStationResponse(lineId, stations);
    }
}
