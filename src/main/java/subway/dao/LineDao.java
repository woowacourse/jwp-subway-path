package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
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

    public LineEntity insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getLineName().getName());
        params.put("color", line.getLineColor().getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, line.getLineName().getName(), line.getLineColor().getColor());
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(Long id, Line line) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{line.getLineName().getName(), line.getLineColor().getColor(), id});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
