package subway.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.station.Station;

import java.util.List;

public class DBSectionRepository implements SectionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Section> sectionRowMapper =
            (rs, rowNum) -> {
                Long sectionId = rs.getLong("section_id");
                Line line = new Line(rs.getLong("line_id"), rs.getString("line_name"));
                Station upStation = new Station(rs.getLong("up_station_id"), rs.getString("up_station_name"), line);
                Station downStation = new Station(rs.getLong("down_station_id"), rs.getString("down_station_name"), line);
                int distance = rs.getInt("distance");
                return new Section(sectionId, upStation, downStation, distance);
            };

    public DBSectionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Section> findSectionsContaining(Section sectionToAdd) {
        String upStationName = sectionToAdd.getUpStation().getName();
        String downStationName = sectionToAdd.getDownStation().getName();
        Long lineId = sectionToAdd.getLineId();
        String sql = "SELECT\n" +
                "ss.id AS section_id,\n" +
                "us.id AS up_station_id,\n" +
                "us.name AS up_station_name,\n" +
                "ds.id AS down_station_id,\n" +
                "ds.name AS down_station_name,\n" +
                "ss.distance\n" +
                "ss.line_id,\n" +
                "line.name AS line_name\n" +
                "FROM\n" +
                "section ss\n" +
                "INNER JOIN station us ON us.line_id = ss.line_id AND us.id = ss.up_station_id\n" +
                "INNER JOIN station ds ON ds.line_id = ss.line_id AND ds.id = ss.down_station_id\n" +
                "INNER JOIN line ss.line_id == line.id\n" +
                "WHERE (up_station_name = ? OR up_station_name = ? OR down_station_name = ? OR down_station_name = ?) " +
                "AND line_id = ?";

        return jdbcTemplate.query(sql, sectionRowMapper, upStationName, downStationName, upStationName, downStationName, lineId);
    }

    @Override
    public List<Section> findSectionsByLineId(Long lineId) {
        String sql = "SELECT\n" +
                "ss.id AS section_id,\n" +
                "us.id AS up_station_id,\n" +
                "us.name AS up_station_name,\n" +
                "ds.id AS down_station_id,\n" +
                "ds.name AS down_station_name,\n" +
                "ss.distance\n" +
                "ss.line_id,\n" +
                "line.name AS line_name\n" +
                "FROM\n" +
                "section ss\n" +
                "INNER JOIN station us ON us.line_id = ss.line_id AND us.id = ss.up_station_id\n" +
                "INNER JOIN station ds ON ds.line_id = ss.line_id AND ds.id = ss.down_station_id\n" +
                "INNER JOIN line ss.line_id == line.id\n" +
                "WHERE line_id = ?";

        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }
}
