package subway.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.LineSectionDao;
import subway.dao.LineStationDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.dto.RegisterInnerStationRequest;
import subway.dto.RegisterStationsRequest;
import subway.entity.LineEntity;
import subway.entity.LineSectionEntity;
import subway.entity.LineStationEntity;
import subway.entity.SectionEntity;

@Repository
public class LineRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineStationDao lineStationDao;
    private final LineSectionDao lineSectionDao;

    public LineRepository(JdbcTemplate jdbcTemplate, LineDao lineDao, StationDao stationDao, SectionDao sectionDao,
        final LineStationDao lineStationDao, final LineSectionDao lineSectionDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineStationDao = lineStationDao;
        this.lineSectionDao = lineSectionDao;
    }

    public Line findByName(final String name) {
        List<Station> stations = stationDao.findByLineNameJoinLine(name);
        List<Section> sections = findSectionsByLineNameJoinLine(name);
        LineEntity line = lineDao.findByName(name).orElseThrow(RuntimeException::new);
        return new Line(line.getId(), line.getName(), line.getColor(), new Stations(stations), new Sections(sections), line.getUpBoundStationId());
    }

    private List<Section> findSectionsByLineNameJoinLine(final String name) {
        String sectionQuery =
            "SELECT S.ID, S.LEFT_STATION_ID, S.RIGHT_STATION_ID, S.DISTANCE FROM SECTION S, LINE L, LINE_SECTION LS " +
                "WHERE S.ID = LS.SECTION_ID AND L.ID = LS.LINE_ID " +
                "AND L.NAME = ?";

        List<Section> sections = jdbcTemplate.query(sectionQuery,
            (rs, rowNum) -> new Section(
                rs.getLong("ID"),
                stationDao.findById(rs.getLong("LEFT_STATION_ID")),
                stationDao.findById(rs.getLong("RIGHT_STATION_ID")),
                rs.getInt("DISTANCE")),
            name);
        return sections;
    }

    public void saveInitStations(Station leftStation, Station rightStation, Line line, int distance) {
        lineStationDao.insert(new LineStationEntity(leftStation.getId(), line.getId()));
        lineStationDao.insert(new LineStationEntity(rightStation.getId(), line.getId()));
        SectionEntity sectionEntity = sectionDao.insert(
            new SectionEntity(leftStation.getId(), rightStation.getId(), distance));
        lineSectionDao.insert(new LineSectionEntity(line.getId(), sectionEntity.getId()));
        LineEntity newLine = new LineEntity(
            line.getId(),
            line.getName(),
            line.getColor(),
            leftStation.getId(),
            rightStation.getId()
        );
        updateBoundStations(newLine);
    }

    public void saveBoundStation(Line line, Station station ,Long sectionId) {
        lineSectionDao.insert(new LineSectionEntity(line.getId(), sectionId));
        lineStationDao.insert(new LineStationEntity(station.getId(), line.getId()));
    }

    public List<Station> findStations(String leftStationName, String rightStationName) {
        return List.of(
            stationDao.findByName(leftStationName).orElseThrow(RuntimeException::new),
            stationDao.findByName(rightStationName).orElseThrow(RuntimeException::new)
        );
    }

    public LineEntity insert(Line line) {
        return lineDao.insert(line);
    }

    public List<LineEntity> findAll() {
        return lineDao.findAll();
    }


    public void updateBoundStations(LineEntity newLine) {
        lineDao.updateBoundStations(newLine);
    }

}
