package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> lineEntityRowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(final LineEntity newLine) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", newLine.getName());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<LineEntity> findAll() {
        final String sql = "select id, name from LINE";
        return jdbcTemplate.query(sql, lineEntityRowMapper);
    }

    public LineEntity findById(final Long id) {
        final String sql = "select id, name from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, lineEntityRowMapper, id);
    }

    public void updateName(final LineEntity line) {
        final String sql = "update LINE set name = ? where id = ?";
        jdbcTemplate.update(sql, line.getName(), line.getId());
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
