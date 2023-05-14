package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DbLineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    public final RowMapper<LineEntity> rowMapper = (resultSet, rowNum) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );

    public DbLineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity saveLine(LineEntity lineEntity) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", lineEntity.getName());
        final long id = insertAction.executeAndReturnKey(parameters).longValue();
        return new LineEntity(id, lineEntity.getName());

    }

    public LineEntity findByName(final String name) {
        final String sql = "SELECT * FROM line WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public LineEntity findById(final Long id) {
        final String sql = "SELECT * FROM line WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteLine(Long id) {
        final String sql = "delete from line where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
