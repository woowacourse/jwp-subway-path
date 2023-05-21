package subway.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.PathDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.LineName;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Repository
public class SubwayRepository {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final PathDao pathDao;

    @Autowired
    public SubwayRepository(StationDao stationDao, LineDao lineDao, SectionDao sectionDao, PathDao pathDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.pathDao = pathDao;
    }

    public Stations getStations() {
        List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(stationEntity -> new Station(stationEntity.getName()))
                .collect(collectingAndThen(toSet(), Stations::new));
    }

    public Line getLineByName(LineName lineName) {
        long lineId = lineDao.findIdByName(lineName.getName())
                .orElseThrow(() -> new LineNotFoundException("존재하지 않는 노선입니다."));
        List<SectionEntity> sectionsOfLineName = sectionDao.findSectionsByLineId(lineId);
        return toLine(lineName.getName(), sectionsOfLineName);
    }

    private Line toLine(String lineName, List<SectionEntity> sectionsOfLineName) {
        return sectionsOfLineName.stream()
                .map(sectionEntity -> {
                    long upstreamId = sectionEntity.getUpstreamId();
                    long downstreamId = sectionEntity.getDownstreamId();
                    return new Section(findStation(upstreamId), findStation(downstreamId), sectionEntity.getDistance());
                })
                .collect(collectingAndThen(toList(), (sections) -> new Line(new LineName(lineName), sections)));
    }

    public Station findStation(long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException("찾는 역이 존재하지 않습니다."));
        return new Station(stationEntity.getName());
    }

    public long addStation(Station stationToAdd) {
        StationEntity stationEntity = new StationEntity.Builder()
                .name(stationToAdd.getName())
                .build();
        return stationDao.insert(stationEntity)
                .getId();
    }

    public Optional<Long> findStationIdByName(String name) {
        return stationDao.findIdByName(name);
    }

    public long updateLine(Line line) {
        long lineId = lineDao.findIdByName(line.getName().getName())
                .orElseThrow(() -> new NoSuchElementException("디버깅: 노선 이름에 해당하는 노선이 없습니다."));
        sectionDao.deleteAll(lineId);
        addSectionsToLine(line.getSectionsWithoutEndPoints(), lineId);
        return lineId;
    }

    private void addSectionsToLine(List<Section> sections, long lineId) {
        List<SectionEntity> sectionEntities = sections.stream()
                .map(section -> toSectionEntity(section, lineId))
                .collect(toList());

        sectionDao.insertAll(sectionEntities);
    }

    private SectionEntity toSectionEntity(Section section, long lineId) {
        long upstreamId = findStationIdByName(section.getUpstream().getName())
                .orElseThrow(() -> new IllegalArgumentException("디버깅: 등록되지 않은 역이 section으로 만들어졌습니다"));
        long downstreamId = findStationIdByName(section.getDownstream().getName())
                .orElseThrow(() -> new IllegalArgumentException("디버깅: 등록되지 않은 역이 section으로 만들어졌습니다"));
        return new SectionEntity.Builder()
                .upstreamId(upstreamId)
                .downstreamId(downstreamId)
                .distance(section.getDistance())
                .lineId(lineId)
                .build();
    }

    public Optional<Line> getLineById(long id) {
        List<SectionEntity> sections = sectionDao.findSectionsByLineId(id);
        Optional<LineEntity> lineEntity = lineDao.findById(id);
        return lineEntity.map(entity -> toLine(entity.getName(), sections));
    }

    public Lines getLines() {
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> toLine(lineEntity.getName(), sectionDao.findSectionsByLineId(lineEntity.getId())))
                .collect(collectingAndThen(toList(), Lines::new));
    }

    public long addNewLine(Line newLine) {
        addLine(newLine.getName());
        return updateLine(newLine);
    }

    public long addLine(LineName lineName) {
        return lineDao.insert(
                new LineEntity.Builder()
                        .name(lineName.getName())
                        .build()
        );
    }
}
