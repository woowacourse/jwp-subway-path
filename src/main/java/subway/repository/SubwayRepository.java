package subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;

@Repository
public class SubwayRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    @Autowired
    public SubwayRepository(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Optional<LineEntity> findLineByName(final String name) {
        return lineDao.findByName(name);
    }

    public List<Station> findStations() {
        final List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(stationEntity -> new Station(stationEntity.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    public Line getSectionsByLineName(final String name) {
        final LineEntity lineEntity = lineDao.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineEntity.getId());
        return toLine(name, lineEntity.getColor(), sectionEntities);
    }

    public Line getLineByName(final String name) {
        final LineEntity lineEntity = lineDao.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        List<SectionEntity> sectionsOfLineName = sectionDao.findByLineId(lineEntity.getId());
        return toLine(name, lineEntity.getColor(), sectionsOfLineName);
    }

    public void addStation(final Station station) {
        stationDao.insert(station.getName());
    }

    private SectionEntity toSectionEntity(final Section section, long lineId) {
        final Long sourceId = findStationIdByName(section.getSource().getName());
        final Long targetId = findStationIdByName(section.getTarget().getName());
        return new SectionEntity(lineId, sourceId, targetId, section.getDistance());
    }

    public Long findStationIdByName(final String name) {
        final StationEntity stationEntity = stationDao.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 가지지 않았습니다"));
        return stationEntity.getId();
    }

    public Line getLineById(final Long id) {
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(id);
        final LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
        return toLine(lineEntity.getName(), lineEntity.getColor(), sectionEntities);
    }

    private Line toLine(final String name, final String color, final List<SectionEntity> sectionEntities) {
        final List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> {
                    final Long sourceStationId = sectionEntity.getSourceStationId();
                    final Long downstreamId = sectionEntity.getTargetStationId();
                    return new Section(toStation(sourceStationId), toStation(downstreamId), sectionEntity.getDistance());
                })
                .collect(Collectors.toList());
        return new Line(name, color, sections);
    }

    private Station toStation(final Long stationId) {
        final StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("서버에러: 해당 id를 가진 역이 없습니다."));
        return new Station(stationEntity.getName());
    }

    public Subway getSubway() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<Line> lines = lineEntities.stream()
                .map(lineEntity -> toLine(
                        lineEntity.getName(),
                        lineEntity.getColor(),
                        sectionDao.findByLineId(lineEntity.getId())
                ))
                .collect(Collectors.toList());
        return new Subway(lines);
    }

    public Long addNewLine(final Line line) {
        addLine(line.getName(), line.getColor());
        return updateLine(line);
    }

    public Long addLine(final String name, final String color) {
        return lineDao.insert(name, color);
    }

    public Long updateLine(final Line line) {
        final LineEntity lineEntity = lineDao.findByName(line.getName())
                .orElseThrow(() -> new IllegalArgumentException("서버 에러: 노선 이름에 해당하는 노선이 없습니다."));
        sectionDao.deleteByLineId(lineEntity.getId());
        addSectionsToLine(line.sections(), lineEntity.getId());
        return lineEntity.getId();
    }

    private void addSectionsToLine(final List<Section> sections, final Long lineId) {
        List<SectionEntity> sectionEntities = sections.stream()
                .map(section -> toSectionEntity(section, lineId))
                .collect(Collectors.toList());
        sectionDao.insertAll(sectionEntities);
    }
}
