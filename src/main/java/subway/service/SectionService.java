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

    public void save(SectionCreateRequest sectionCreateRequest) {
        Long lineId = sectionCreateRequest.getLineId();
        Line line = makeLine(lineId);

        List<Section> originalSections = line.getSectionsByList();

        StationEntity upStationEntity = findStationByName(sectionCreateRequest.getUpStation());
        StationEntity downStationEntity = findStationByName(sectionCreateRequest.getDownStation());

        line.addSection(
                new Station(upStationEntity.getId(), upStationEntity.getName()),
                new Station(downStationEntity.getId(), downStationEntity.getName()),
                sectionCreateRequest.getDistance()
        );

        saveNewSections(lineId, originalSections, line);
        deleteOriginalSection(lineId, originalSections, line);
    }

    public void delete(SectionDeleteRequest sectionDeleteRequest) {
        StationEntity stationEntity = findStationByName(sectionDeleteRequest.getName());
        Long lineId = sectionDeleteRequest.getLindId();
        Line line = makeLine(lineId);

        List<Section> originalSections = line.getSectionsByList();

        line.deleteSection(new Station(stationEntity.getId(), stationEntity.getName()));

        if (line.isEmpty()) {
            sectionDao.deleteByLineId(lineId);
            return;
        }

        saveNewSections(lineId, originalSections, line);
        deleteOriginalSection(lineId, originalSections, line);
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

    private void saveNewSections(long lineId, List<Section> originalSections, Line line) {
        List<Section> newSections = line.getSectionsByList();
        newSections.removeAll(originalSections);
        for (Section newSection : newSections) {
            sectionDao.save(new SectionEntity(
                    newSection.getUpStation().getId(),
                    newSection.getDownStation().getId(),
                    lineId,
                    newSection.getDistance())
            );
        }
    }

    private void deleteOriginalSection(long lineId, List<Section> originalSections, Line line) {
        if (originalSections.isEmpty()) {
            return;
        }
        List<Section> newSections = line.getSectionsByList();
        originalSections.removeAll(newSections);
        Section deletedSection = originalSections.get(0);
        sectionDao.delete(new SectionEntity(
                deletedSection.getUpStation().getId(),
                deletedSection.getDownStation().getId(),
                lineId,
                deletedSection.getDistance())
        );
    }
}
