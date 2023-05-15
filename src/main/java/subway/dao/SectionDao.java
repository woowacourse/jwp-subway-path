package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("start_station_id"),
                    rs.getLong("end_station_id"),
                    rs.getInt("distance"),
                    rs.getLong("line_id")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SectionEntity> findAll() {
        final String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void insertAll(final List<SectionEntity> sections) {
        final String sql = "INSERT INTO section (start_station_id, end_station_id, distance, line_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, sections, sections.size(), ((ps, section) -> {
            ps.setLong(1, section.getStartStationId());
            ps.setLong(2, section.getEndStationId());
            ps.setInt(3, section.getDistance());
            ps.setLong(4, section.getLineId());
        }));
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
