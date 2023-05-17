package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.dto.SectionStationResultMap;

import java.util.List;

@Repository
public class SectionStationDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SectionStationResultMap> resultMapRowMapper = (rs, num) -> new SectionStationResultMap(
            rs.getLong("sectionId"),
            rs.getInt("distance"),
            rs.getLong("up_station_id"),
            rs.getString("upStationName"),
            rs.getLong("down_station_id"),
            rs.getString("downStationName"),
            rs.getLong("lineId")
    );

    public SectionStationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SectionStationResultMap> findAllByLineId(Long lineId) {
        final String sql = "SELECT se.id sectionId, " +
                "se.distance distance, " +
                "se.up_station_id, " +
                "st1.name upStationName, " +
                "se.down_station_id, " +
                "st2.name downStationName, " +
                "se.line_id lineId " +
                "FROM section se " +
                "JOIN station st1 ON st1.id = se.up_station_id " +
                "JOIN station st2 ON st2.id = se.down_station_id " +
                "WHERE line_id = ?";

        return jdbcTemplate.query(sql, resultMapRowMapper, lineId);
    }

    public List<SectionStationResultMap> findAll() {
        final String sql = "SELECT se.id sectionId, " +
                "se.distance distance, " +
                "se.up_station_id, " +
                "st1.name upStationName, " +
                "se.down_station_id, " +
                "st2.name downStationName, " +
                "se.line_id lineId " +
                "FROM section se " +
                "JOIN station st1 ON st1.id = se.up_station_id " +
                "JOIN station st2 ON st2.id = se.down_station_id";

        return jdbcTemplate.query(sql, resultMapRowMapper);
    }
}
