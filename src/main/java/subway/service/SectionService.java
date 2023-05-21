package subway.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionCreateRequest;
import subway.dto.SectionDeleteRequest;
import subway.exception.ErrorCode;
import subway.exception.NoSuchException;

@Service
@Transactional
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public Long save(SectionCreateRequest sectionCreateRequest) {
        Long lineId = sectionCreateRequest.getLineId();
        Line line = makeLine(lineId);
        StationEntity upStationEntity = findStationByName(sectionCreateRequest.getUpStation());
        StationEntity downStationEntity = findStationByName(sectionCreateRequest.getDownStation());
        line.addSection(
                new Station(upStationEntity.getId(), upStationEntity.getName()),
                new Station(downStationEntity.getId(), downStationEntity.getName()),
                sectionCreateRequest.getDistance()
        );

        sectionDao.deleteByLineId(lineId);
        sectionDao.batchSave(getModifySections(lineId, line));

        return lineId;
    }

    public void delete(SectionDeleteRequest sectionDeleteRequest) {
        Long lineId = sectionDeleteRequest.getLineId();
        Line line = makeLine(lineId);
        StationEntity stationEntity = findStationByName(sectionDeleteRequest.getName());
        line.deleteSection(new Station(stationEntity.getId(), stationEntity.getName()));

        sectionDao.deleteByLineId(lineId);
        sectionDao.batchSave(getModifySections(lineId, line));
    }

    private List<SectionEntity> getModifySections(Long lineId, Line line) {
        return line.getSectionsByList().stream()
                .map(section -> new SectionEntity(
                        section.getUpStation().getId(),
                        section.getDownStation().getId(),
                        lineId,
                        section.getDistance()))
                .collect(Collectors.toList());
    }

    private StationEntity findStationByName(String stationName) {
        try {
            return stationDao.findByName(stationName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NoSuchException(ErrorCode.NO_SUCH_STATION);
        }
    }

    private Line makeLine(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        List<Section> sections = makeSections(lineId);

        return Line.of(lineEntity.getName(), sections);
    }

    private List<Section> makeSections(Long id) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        Map<Long, String> stationEntities = stationDao.findAll().stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        StationEntity::getName)
                );

        return sectionEntities.stream()
                .map(entity -> new Section(
                        new Station(entity.getUpStationId(), stationEntities.get(entity.getUpStationId())),
                        new Station(entity.getDownStationId(), stationEntities.get(entity.getDownStationId())),
                        entity.getDistance()
                )).collect(Collectors.toList());
    }
}
