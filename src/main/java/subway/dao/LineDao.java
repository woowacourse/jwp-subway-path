package subway.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, lineEntity.getName(), lineEntity.getColor());
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name, color FROM line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(Long id) {
        String sql = "SELECT id, name, color FROM line WHERE id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, rowMapper));
        }
        catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByName(String name) {
        String sql = "SELECT id, name, color FROM line WHERE name = :name";
        SqlParameterSource source = new MapSqlParameterSource("name", name);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, rowMapper));
        }
        catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByColor(String color) {
        String sql = "SELECT id, name, color FROM line WHERE color = :color";
        SqlParameterSource source = new MapSqlParameterSource("color", color);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, rowMapper));
        }
        catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(LineEntity newLineEntity) {
        String sql = "UPDATE line SET name = :name, color = :color WHERE id = :id";
        SqlParameterSource source = new BeanPropertySqlParameterSource(newLineEntity);
        jdbcTemplate.update(sql, source);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM line WHERE id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, source);
    }

    public boolean isExistId(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM line WHERE id = :id)";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(sql, source, Boolean.class);
    }

    public boolean isExistName(String name) {
        String sql = "SELECT EXISTS (SELECT 1 FROM line WHERE name = :name)";
        SqlParameterSource source = new MapSqlParameterSource("name", name);
        return jdbcTemplate.queryForObject(sql, source, Boolean.class);
    }

    public boolean isExistColor(String color) {
        String sql = "SELECT EXISTS (SELECT 1 FROM line WHERE color = :color)";
        SqlParameterSource source = new MapSqlParameterSource("color", color);
        return jdbcTemplate.queryForObject(sql, source, Boolean.class);
    }

}
