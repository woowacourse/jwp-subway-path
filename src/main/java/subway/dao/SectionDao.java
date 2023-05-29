package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import subway.entity.SectionEntity;

@Component
public class SectionDao implements Dao<SectionEntity> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getString("start_station_name"),
                    rs.getString("end_station_name"),
                    rs.getInt("distance"),
                    rs.getLong("line_id")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SectionEntity insert(SectionEntity section) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(SectionEntity sectionEntity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<SectionEntity> findById(Long id) {
        String sql = "SELECT s.id, s1.name AS start_station_name, s2.name AS end_station_name, distance, s.line_id "
                + "FROM section s "
                + "JOIN station s1 ON s.start_station_id = s1.id "
                + "JOIN station s2 ON s.end_station_id = s2.id"
                + "WEHRE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<SectionEntity> findAll() {
        final String sql =
                "SELECT s.id, s1.name AS start_station_name, s2.name AS end_station_name, distance, s.line_id "
                        + "FROM section s "
                        + "JOIN station s1 ON s.start_station_id = s1.id "
                        + "JOIN station s2 ON s.end_station_id = s2.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void insertAll(final List<SectionEntity> sections) {
        final String sql = "INSERT INTO section (start_station_id, end_station_id, distance, line_id) VALUES ("
                + "(SELECT id FROM station WHERE name = ? and line_id = ?), "
                + "(SELECT id FROM station WHERE name = ? and line_id = ?), "
                + "?, "
                + "?)";
        jdbcTemplate.batchUpdate(sql, sections, sections.size(), ((ps, section) -> {
            ps.setString(1, section.getStartStationName());
            ps.setLong(2, section.getLineId());
            ps.setString(3, section.getEndStationName());
            ps.setLong(4, section.getLineId());
            ps.setInt(5, section.getDistance());
            ps.setLong(6, section.getLineId());
        }));
    }

    public void deleteAll(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql =
                "SELECT s.id, s1.name AS start_station_name, s2.name AS end_station_name, distance, s.line_id "
                        + "FROM section s "
                        + "JOIN station s1 ON s.start_station_id = s1.id "
                        + "JOIN station s2 ON s.end_station_id = s2.id "
                        + "WHERE s.line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }
}
