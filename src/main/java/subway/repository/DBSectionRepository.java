package subway.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.station.Station;

import java.util.List;

@Repository
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
        String sql = "SELECT " +
                "ss.id AS section_id, " +
                "us.id AS up_station_id, " +
                "us.name AS up_station_name, " +
                "ds.id AS down_station_id, " +
                "ds.name AS down_station_name, " +
                "ss.distance, " +
                "ss.line_id, " +
                "line.name AS line_name " +
                "FROM " +
                "section ss " +
                "INNER JOIN station us ON us.line_id = ss.line_id AND us.id = ss.up_station_id " +
                "INNER JOIN station ds ON ds.line_id = ss.line_id AND ds.id = ss.down_station_id " +
                "INNER JOIN line ON ss.line_id = line.id " +
                "WHERE (us.name = ? OR us.name = ? OR ds.name = ? OR ds.name = ?) " +
                "AND line.id = ?";

        return jdbcTemplate.query(sql, sectionRowMapper, upStationName, downStationName, upStationName, downStationName, lineId);
    }

    @Override
    public List<Section> findSectionsByLineId(Long lineId) {
        String sql = "SELECT " +
                "ss.id AS section_id, " +
                "us.id AS up_station_id, " +
                "us.name AS up_station_name, " +
                "ds.id AS down_station_id, " +
                "ds.name AS down_station_name, " +
                "ss.distance, " +
                "ss.line_id, " +
                "line.name AS line_name " +
                "FROM " +
                "section ss " +
                "INNER JOIN station us ON us.line_id = ss.line_id AND us.id = ss.up_station_id " +
                "INNER JOIN station ds ON ds.line_id = ss.line_id AND ds.id = ss.down_station_id " +
                "INNER JOIN line ON ss.line_id = line.id " +
                "WHERE line.id = ?";

        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }
}
