package subway.service;

import java.util.List;
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
import subway.dto.StationCreateRequest;
import subway.dto.StationDeleteRequest;

@Service
@Transactional
public class StationService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationService(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public void save(final StationCreateRequest stationCreateRequest) {
        long lineId = stationCreateRequest.getLineId();
        Line line = makeLine(lineId);

        List<Station> originalStations = line.getStations();
        List<Section> originalSections = line.getSectionsByList();

        line.addSection(
                new Station(stationCreateRequest.getUpStation()),
                new Station(stationCreateRequest.getDownStation()),
                stationCreateRequest.getDistance()
        );

        saveNewStation(lineId, originalStations, line);
        saveNewSections(lineId, originalSections, line);
        deleteOriginalSection(lineId, originalSections, line);
    }

    private Line makeLine(final long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        List<Section> sections = makeSections(lineId);

        return Line.of(lineEntity.getName(), sections);
    }

    private void saveNewStation(final long lineId, final List<Station> originalStations, final Line line) {
        List<Station> newStations = line.getStations();
        newStations.removeAll(originalStations);

        for (Station newStation : newStations) {
            stationDao.save(new StationEntity(newStation.getName(), lineId));
        }
    }

    private void saveNewSections(final long lineId, final List<Section> originalSections, final Line line) {
        List<Section> newSections = line.getSectionsByList();
        newSections.removeAll(originalSections);
        for (Section newSection : newSections) {
            StationEntity newUpStation = stationDao.findByName(newSection.getUpStation().getName());
            StationEntity newDownStation = stationDao.findByName(newSection.getDownStation().getName());

            sectionDao.save(new SectionEntity(
                    newUpStation.getId(),
                    newDownStation.getId(),
                    lineId,
                    newSection.getDistance())
            );
        }
    }

    private void deleteOriginalSection(final long lineId, final List<Section> originalSections, final Line line) {
        if (originalSections.isEmpty()) {
            return;
        }
        List<Section> newSections = line.getSectionsByList();
        originalSections.removeAll(newSections);
        Section deletedSection = originalSections.get(0);
        StationEntity deletedUpStation = stationDao.findByName(deletedSection.getUpStation().getName());
        StationEntity deletedDownStation = stationDao.findByName(deletedSection.getDownStation().getName());

        sectionDao.delete(new SectionEntity(
                deletedUpStation.getId(),
                deletedDownStation.getId(),
                lineId,
                deletedSection.getDistance())
        );
    }

    private List<Section> makeSections(final Long id) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);

        return sectionEntities.stream()
                .map(entity -> new Section(
                        new Station(stationDao.findById(entity.getUpStationId()).getName()),
                        new Station(stationDao.findById(entity.getDownStationId()).getName()),
                        entity.getDistance()
                )).collect(Collectors.toList());
    }

    public void delete(final StationDeleteRequest stationDeleteRequest) {
        StationEntity stationEntity = stationDao.findByName(stationDeleteRequest.getName());
        Long lineId = stationEntity.getLineId();
        Line line = makeLine(lineId);
        List<Section> originalSections = line.getSectionsByList();

        line.deleteStation(new Station(stationEntity.getName()));

        if (line.isEmpty()) {
            stationDao.deleteByLineId(lineId);
            sectionDao.deleteByLineId(lineId);
            lineDao.deleteById(lineId);
            return;
        }

        stationDao.deleteByName(stationEntity.getName());
        saveNewSections(lineId, originalSections, line);
        deleteOriginalSection(lineId, originalSections, line);
    }
}
