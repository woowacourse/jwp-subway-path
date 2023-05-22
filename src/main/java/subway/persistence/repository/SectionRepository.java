package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.LineStationRequest;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.SectionWithStationNameEntity;
import subway.persistence.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionRepository {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionRepository(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Section toSection(Long lineId, LineStationRequest lineStationRequest) {
        Line line = findLineById(lineId);

        Station preStation = findStationById(lineStationRequest.getPreStationId());
        Station station = findStationById(lineStationRequest.getStationId());

        return new Section(line, preStation, station, new Distance(lineStationRequest.getDistance()));
    }

    public Sections getCurrentLineSections(Long lineId) {
        Optional<LineEntity> lineEntity = lineDao.findById(lineId);
        if (lineEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다");
        }
        Line line = lineEntity.get().toLine();
        List<SectionWithStationNameEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        List<Section> sections = new ArrayList<>();
        for (SectionWithStationNameEntity entity : sectionEntities) {
            sections.add(new Section(line, new Station(entity.getPreStationName()),
                    new Station(entity.getStationName()), new Distance(entity.getDistance())));
        }
        return new Sections(sections);
    }

    public Long saveSection(Long lineId, LineStationRequest lineStationRequest) {
        SectionEntity sectionEntity = new SectionEntity(lineId,
                lineStationRequest.getPreStationId(), lineStationRequest.getStationId(), lineStationRequest.getDistance());
        return sectionDao.save(sectionEntity);
    }

    public void updateSectionAfterAddition(Long lineId, Section section, LineStationRequest lineStationRequest) {
        Long preStationId = findStationIdByStationName(section.getPreStation().getName());
        Long postStationId = findStationIdByStationName(section.getStation().getName());
        SectionEntity sectionEntity = new SectionEntity(lineId, preStationId, postStationId, (long) section.getDistance().getDistance());
        if (preStationId.equals(lineStationRequest.getStationId())) {
            sectionDao.updatePreStation(sectionEntity);
        }
        if (postStationId.equals(lineStationRequest.getPreStationId())) {
            sectionDao.updateStation(sectionEntity);
        }
    }

    public Station findStationById(Long id) {
        Optional<StationEntity> stationEntity = stationDao.findById(id);
        if (stationEntity.isPresent()) {
            return stationEntity.get().toStation();
        }
        throw new IllegalArgumentException("존재하지 않는 역입니다");
    }

    public void removeSection(Long stationId) {
        sectionDao.remove(stationId);
    }

    public void updateSectionAfterRemoval(Long lineId, Section section) {
        Long preStationId = findStationIdByStationName(section.getPreStation().getName());
        Long postStationId = findStationIdByStationName(section.getStation().getName());
        sectionDao.save(new SectionEntity(lineId,
                preStationId, postStationId, (long) section.getDistance().getDistance()));
    }

    private Line findLineById(Long lineId) {
        Optional<LineEntity> lineEntity = lineDao.findById(lineId);
        if (lineEntity.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다");
        }
        return lineEntity.get().toLine();
    }

    private Long findStationIdByStationName(String name) {
        return stationDao.findIdByName(name);
    }
}
