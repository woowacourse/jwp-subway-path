package subway.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;

import java.util.List;

@Repository
public class LineRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineRepository(JdbcTemplate jdbcTemplate, LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Line findByName(final String name) {
        String stationQuery =
                "SELECT S.ID, S.NAME FROM STATION S, LINE L, LINE_STATION LS " +
                        "WHERE S.ID = LS.STATION_ID AND L.ID = LS.LINE_ID " +
                        "AND L.NAME = ?";

        List<Station> stations = jdbcTemplate.query(stationQuery,
                (rs, rowNum) -> new Station(
                        rs.getLong("ID"),
                        rs.getString("NAME")),
                name);

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

        Line line = lineDao.findByName(name).orElseThrow(RuntimeException::new);

        String upBoundStationIdQuery =
                "SELECT UPBOUND_STATION_ID FROM LINE WHERE NAME = ?";
        Long upBoundStationId = jdbcTemplate.queryForObject(upBoundStationIdQuery, Long.class, name);
        return new Line(line.getId(), line.getName(), line.getColor(), new Stations(stations), new Sections(sections), upBoundStationId);
    }
}
