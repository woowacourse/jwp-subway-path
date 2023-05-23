package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.LineEntity;

import javax.sql.DataSource;
import java.util.List;

@Component
public class LineDao {

    private static final RowMapper<LineEntity> lineEntityRowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(final LineEntity newLine) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(newLine);
        return insertAction.executeAndReturnKey(source).longValue();
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
