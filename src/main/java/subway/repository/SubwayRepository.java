package subway.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Repository
public class SubwayRepository {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    @Autowired
    public SubwayRepository(StationDao stationDao, LineDao lineDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Stations getStations() {
        List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(stationEntity -> Station.from(stationEntity.getName()))
                .collect(collectingAndThen(toSet(), Stations::new));
    }

    public Line getLineByName(String lineName) {
        Optional<Long> found = lineDao.findIdByName(lineName);
        long lineId = found.orElseThrow(() -> new NoSuchElementException("디버깅: 노선 이름에 해당하는 노선이 없습니다."));
        List<SectionEntity> sectionsOfLineName = sectionDao.findSectionsByLineId(lineId);
        return toLine(lineName, sectionsOfLineName);
    }

    private Station findStation(long stationId) {
        return Station.from(stationDao.findById(stationId).getName());
    }

    public void addStation(Station stationToAdd) {
        StationEntity stationEntity = new StationEntity.Builder()
                .name(stationToAdd.getName())
                .build();

        stationDao.insert(stationEntity);
    }

    public Optional<Long> findStationIdByName(String name) {
        return stationDao.findIdByName(name);
    }

    public Long updateLine(Line line) {
        long lineId = lineDao.findIdByName(line.getName()).orElseThrow(() -> new NoSuchElementException("디버깅: 노선 이름에 해당하는 노선이 없습니다."));
        sectionDao.deleteAll(lineId);
        addSectionsToLine(line.getSections(), lineId);
        return lineId;
    }

    private void addSectionsToLine(List<Section> sections, long lineId) {
        List<SectionEntity> sectionEntities = sections.stream()
                .map(section -> toSectionEntity(section, lineId))
                .collect(toList());

        sectionDao.insertAll(sectionEntities);
    }

    private SectionEntity toSectionEntity(Section section, long lineId) {
        Long upstreamId = findStationIdByName(section.getUpstream().getName()).orElseThrow(() -> new IllegalArgumentException("디버깅: 등록되지 않은 역이 section으로 만들어졌습니다"));
        Long downstreamId = findStationIdByName(section.getDownstream().getName()).orElseThrow(() -> new IllegalArgumentException("디버깅: 등록되지 않은 역이 section으로 만들어졌습니다"));
        return new SectionEntity.Builder()
                .upstreamId(upstreamId)
                .downstreamId(downstreamId)
                .distance(section.getDistance())
                .lineId(lineId)
                .build();
    }

    public LineNames getLineNames() {
        List<String> lineNames = lineDao.findAll().stream()
                .map(LineEntity::getName)
                .collect(toList());

        return new LineNames(lineNames);
    }

    public long addLine(String lineName) {
        return lineDao.insert(new LineEntity.Builder().name(lineName).build());
    }

    public Optional<Line> getLineById(Long id) {
        List<SectionEntity> sections = sectionDao.findSectionsByLineId(id);
        Optional<LineEntity> lineEntity = lineDao.findById(id);
        return lineEntity.map(entity -> toLine(entity.getName(), sections));
    }

    private Line toLine(String lineName, List<SectionEntity> sectionsOfLineName) {
        return sectionsOfLineName.stream()
                .map(sectionEntity -> {
                    long upstreamId = sectionEntity.getUpstreamId();
                    long downstreamId = sectionEntity.getDownstreamId();
                    return new Section(findStation(upstreamId), findStation(downstreamId), sectionEntity.getDistance());
                })
                .collect(collectingAndThen(toList(), (sections) -> new Line(lineName, sections)));
    }

    public List<Line> getLines() {
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> toLine(lineEntity.getName(), sectionDao.findSectionsByLineId(lineEntity.getId())))
                .collect(toList());
    }
}
