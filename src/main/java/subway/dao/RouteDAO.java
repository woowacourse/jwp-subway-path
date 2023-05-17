package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@Repository
public class RouteDAO {
    
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Station> stationRowMapper = (rs, rowNum) -> new Station(
            rs.getLong("id"),
            rs.getString("name")
    );
    
    public RouteDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<Station> findAllStationsInLine(final long lineId) {
        final String sql = "SELECT DISTINCT s.id, s.name "
                + "FROM STATION s "
                + "JOIN SECTION sec ON s.id = sec.up_station_id OR s.id = sec.down_station_id "
                + "WHERE sec.line_id = ?";
        return this.jdbcTemplate.query(sql, this.stationRowMapper, lineId);
    }
    
    
}
