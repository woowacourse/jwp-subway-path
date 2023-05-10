package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Section> rowMapper = (rs, rowNum) ->
            new Section(
                    new Station(rs.getLong("left_station_id"), rs.getString("left_station_name")),
                    new Station(rs.getLong("right_station_id"), rs.getString("right_station_name")),
                    new Distance(rs.getInt("distance"))
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Line line, Section section) {
        String sql = "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, line.getId(), section.getLeftId(), section.getRightId(), section.getDistance());
    }

    public List<Section> findAll() {
        String sql =
                "select left_station_id, left_st.name as left_station_name, right_station_id, right_st.name as right_station_name, distance from SECTIONS as se"
                        + " LEFT JOIN STATION as left_st"
                        + " ON se.left_station_id = left_st.id"
                        + " LEFT JOIN STATION as right_st"
                        + " ON se.right_station_id = right_st.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Section> findByLineId(Long lineId) {
        String sql =
                "select left_station_id, left_st.name as left_station_name, right_station_id, right_st.name as right_station_name, distance from SECTIONS as se"
                        + " LEFT JOIN STATION as left_st"
                        + " ON se.left_station_id = left_st.id"
                        + " LEFT JOIN STATION as right_st"
                        + " ON se.right_station_id = right_st.id"
                        + " WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteById(Long lineId, Long stationId) {
        String sql = "delete from SECTIONS where (left_station_id = ? or right_station_id = ?) and line_id = ?";
        jdbcTemplate.update(sql, stationId, stationId, lineId);
    }
}
