package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.business.domain.Line;
import subway.business.domain.LineRepository;
import subway.business.domain.Section;
import subway.business.domain.Station;
import subway.exception.NoSuchLineException;
import subway.exception.NoSuchStationException;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public long create(Line line) {
        LineEntity lineEntityToSave = new LineEntity(
                line.getName(),
                line.getUpwardTerminus().getId(),
                line.getDownwardTerminus().getId()
        );
        long lineId = lineDao.insert(lineEntityToSave);

        Section section = line.getSections().get(0);
        createSection(lineId, section);
        return lineId;
    }

    @Override
    public Line findById(Long id) {
        LineEntity lineEntity = lineDao.findById(id).orElseThrow(NoSuchLineException::new);
        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineEntity.getId());

        List<Section> sections = mapSectionEntitiesToSections(sectionEntities);
        List<Section> orderedSections = getOrderedSections(lineEntity, sections);
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
    public void update(Line line) {
        LineEntity lineEntityToUpdate = new LineEntity(
                line.getId(),
                line.getName(),
                line.getUpwardTerminus().getId(),
                line.getDownwardTerminus().getId()
        );
        lineDao.update(lineEntityToUpdate);

        sectionDao.deleteAllByLineId(line.getId());

        for (Section section : line.getSections()) {
            createSection(line.getId(), section);
        }
    }

    private void createSection(long lineId, Section section) {
        SectionEntity sectionEntityToSave = new SectionEntity(
                lineId,
                section.getUpwardStation().getId(),
                section.getDownwardStation().getId(),
                section.getDistance()
        );
        sectionDao.insert(sectionEntityToSave);
    }

    private List<Section> mapSectionEntitiesToSections(List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(sectionEntity -> new Section(
                        Station.from(stationDao.findById(sectionEntity.getUpwardStationId())
                                .orElseThrow(NoSuchStationException::new)),
                        Station.from(stationDao.findById(sectionEntity.getDownwardStationId())
                                .orElseThrow(NoSuchStationException::new)),
                        sectionEntity.getDistance()))
                .collect(Collectors.toList());
    }

    private List<Section> getOrderedSections(LineEntity lineEntity, List<Section> sections) {
        Station upwardTerminus = Station.from(stationDao.findById(lineEntity.getUpwardTerminusId())
                .orElseThrow(NoSuchStationException::new));
        Station downwardTerminus = Station.from(stationDao.findById(lineEntity.getDownwardTerminusId())
                .orElseThrow(NoSuchStationException::new));
        return getOrderedSectionsByTerminus(sections, upwardTerminus,
                downwardTerminus);
    }

    private List<Section> getOrderedSectionsByTerminus(
            List<Section> sections, Station upwardTerminus, Station downwardTerminus
    ) {
        List<Section> orderedSections = new ArrayList<>();
        Section sectionToAdd;
        Station nextUpwardStation = upwardTerminus;
        do {
            sectionToAdd = findSectionToAddByUpwardStation(sections, nextUpwardStation);
            orderedSections.add(sectionToAdd);
            nextUpwardStation = sectionToAdd.getDownwardStation();
        } while (!nextUpwardStation.equals(downwardTerminus));
        return orderedSections;
    }

    private Section findSectionToAddByUpwardStation(List<Section> sections, Station upwardStation) {
        return sections.stream()
                .filter(section -> section.getUpwardStation().equals(upwardStation))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("노선을 조회하던 중 서버 내부 에러가 발생했습니다."));
    }
}
