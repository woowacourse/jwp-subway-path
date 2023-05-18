package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.vo.Distance;
import subway.domain.entity.Section;
import subway.domain.entity.Station;

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

    public void insertAllByLineId(final Long lineId, final List<Section> sections) {
        String sql = "insert into SECTIONS (line_id, left_station_id, right_station_id, distance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, sections, sections.size(), (ps, argument) -> {
            ps.setLong(1, lineId);
            ps.setLong(2, argument.getLeftId());
            ps.setLong(3, argument.getRightId());
            ps.setInt(4, argument.getDistance().getValue());
        });
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

    public void deleteByLineId(final Long lineId) {
        String sql = "delete from SECTIONS where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
