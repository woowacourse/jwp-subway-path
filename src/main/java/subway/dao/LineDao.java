package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(LineEntity line) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, line.getName(), line.getColor());
    }

    public Optional<LineEntity> findByName(final String name) {
        String sql = "SELECT id, name, color FROM LINE WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByStationId(Long statoinId) {
        String sql = "SELECT l.id, l.name, l.color FROM STATION s " +
                "JOIN LINE l on s.line_id = l.id " +
                "WHERE s.id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, statoinId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findById(Long id) {
        String sql = "SELECT id, name, color FROM LINE WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name, color FROM LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
