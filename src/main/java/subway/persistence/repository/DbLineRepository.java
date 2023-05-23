package subway.persistence.repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.business.domain.line.Line;
import subway.business.domain.line.LineRepository;
import subway.business.domain.line.Section;
import subway.business.domain.line.Station;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@Repository
public class DbLineRepository implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public DbLineRepository(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Override
    public Line create(Line line) {
        LineEntity lineEntityToSave = new LineEntity(
                line.getName()
        );
        LineEntity savedLineEntity = lineDao.insert(lineEntityToSave);

        Section section = line.getSections().get(0);
        saveInitialSection(savedLineEntity.getId(), section);

        return findById(savedLineEntity.getId());
    }

    @Override
    public Line findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id);
        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineEntity.getId());

        List<Section> sections = mapSectionEntitiesToSections(sectionEntities);
        List<Section> orderedSections = getOrderedSections(sections);
        return new Line(lineEntity.getId(), lineEntity.getName(), orderedSections);
    }

    @Override
    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<Line> lines = new ArrayList<>();
        for (LineEntity lineEntity : lineEntities) {
            lines.add(findById(lineEntity.getId()));
        }
        return lines;
    }

    @Override
    public Line update(Line line) {
        LineEntity lineEntityToUpdate = new LineEntity(
                line.getId(),
                line.getName()
        );
        LineEntity updatedLineEntity = lineDao.update(lineEntityToUpdate);

        sectionDao.deleteAllByLineId(updatedLineEntity.getId());
        stationDao.deleteAllByLineId(updatedLineEntity.getId());

        Long lineId = updatedLineEntity.getId();
        List<Section> sections = line.getSections();

        insertAllStations(lineId, sections);

        for (Section section : line.getSections()) {
            saveSection(lineId, section);
        }

        return findById(lineId);
    }

    @Override
    public Station findStationById(long id) {
        StationEntity stationEntity = stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("입력한 ID와 일치하는 Station이 존재하지 않습니다. "
                        + "(입력한 ID : %s)", id)));
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    @Override
    public Line findLineByStationId(Long stationsId) {
        StationEntity stationEntity = stationDao.findById(stationsId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("입력한 ID와 일치하는 Station이 존재하지 않습니다. "
                        + "(입력한 ID : %s)", stationsId)));
        return findById(stationEntity.getLineId());
    }

    private void insertAllStations(Long lineId, List<Section> sections) {
        for (Section section : sections) {
            stationDao.insert(new StationEntity(
                    lineId,
                    section.getUpwardStation().getName())
            );
        }
        stationDao.insert(new StationEntity(
                lineId,
                sections.get(sections.size() - 1).getDownwardStation().getName())
        );
    }

    private void saveInitialSection(long lineId, Section section) {
        long savedUpwardStationId = stationDao.insert(
                new StationEntity(lineId, section.getUpwardStation().getName())
        );
        long savedDownwardStationId = stationDao.insert(
                new StationEntity(lineId, section.getDownwardStation().getName())
        );

        SectionEntity sectionEntityToSave = new SectionEntity(
                lineId,
                savedUpwardStationId,
                savedDownwardStationId,
                section.getDistance()
        );
        sectionDao.insert(sectionEntityToSave);
    }

    private void saveSection(Long lineId, Section section) {
        StationEntity upwardStationEntity = getUpwardStationEntityOf(lineId, section);
        StationEntity downwardStationEntity = getDownwardStationEntityOf(lineId, section);

        SectionEntity sectionEntityToSave = new SectionEntity(
                lineId,
                upwardStationEntity.getId(),
                downwardStationEntity.getId(),
                section.getDistance()
        );
        sectionDao.insert(sectionEntityToSave);
    }

    private StationEntity getUpwardStationEntityOf(Long lineId, Section section) {
        return stationDao.findByLineIdAndName(
                lineId,
                section.getUpwardStation().getName()
        ).orElseThrow(() -> new IllegalArgumentException(String.format("입력한 Line ID, 이름과 일치하는 Station이 존재하지 않습니다. "
                + "(입력한 Line ID : %d / 입력한 이름 : %s)", lineId, section.getUpwardStation().getName())));
    }

    private StationEntity getDownwardStationEntityOf(Long lineId, Section section) {
        return stationDao.findByLineIdAndName(
                lineId,
                section.getDownwardStation().getName()
        ).orElseThrow(() -> new IllegalArgumentException(String.format("입력한 Line ID, 이름과 일치하는 Station이 존재하지 않습니다. "
                + "(입력한 Line ID : %d / 입력한 이름 : %s)", lineId, section.getUpwardStation().getName())));
    }

    private List<Section> mapSectionEntitiesToSections(List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        sectionEntity.getId(),
                        findStationById(sectionEntity.getUpwardStationId()),
                        findStationById(sectionEntity.getDownwardStationId()),
                        sectionEntity.getDistance()))
                .collect(Collectors.toList());
    }

    private List<Section> getOrderedSections(List<Section> sections) {
        List<Section> orderedSections = new LinkedList<>();
        Section firstSection = sections.get(0);

        Section sectionToAdd = firstSection;
        addSectionsToDownward(sections, orderedSections, sectionToAdd);

        sectionToAdd = findSectionToAddByDownwardStation(sections, firstSection.getDownwardStation());
        addSectionsToUpward(sections, orderedSections, sectionToAdd);

        return orderedSections;
    }

    private void addSectionsToUpward(List<Section> sections, List<Section> orderedSections, Section sectionToAdd) {
        Station nextUpwardStation;
        while (sectionToAdd != null) {
            orderedSections.add(sectionToAdd);
            sections.remove(sectionToAdd);
            nextUpwardStation = sectionToAdd.getUpwardStation();
            sectionToAdd = findSectionToAddByDownwardStation(sections, nextUpwardStation);
        }
    }

    private void addSectionsToDownward(List<Section> sections, List<Section> orderedSections, Section sectionToAdd) {
        Station nextUpwardStation;
        do {
            orderedSections.add(sectionToAdd);
            sections.remove(sectionToAdd);
            nextUpwardStation = sectionToAdd.getDownwardStation();
            sectionToAdd = findSectionToAddByUpwardStation(sections, nextUpwardStation);
        } while (sectionToAdd != null);
    }

    private Section findSectionToAddByUpwardStation(List<Section> sections, Station upwardStation) {
        return sections.stream()
                .filter(section -> section.getUpwardStation().hasNameOf(upwardStation.getName()))
                .findFirst()
                .orElse(null);
    }

    private Section findSectionToAddByDownwardStation(List<Section> sections, Station downwardStation) {
        return sections.stream()
                .filter(section -> section.getDownwardStation().hasNameOf(downwardStation.getName()))
                .findFirst()
                .orElse(null);
    }
}
