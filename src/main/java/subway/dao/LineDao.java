package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.domain.Line;
import subway.domain.LineColor;
import subway.domain.LineName;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("lines")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<LineEntity> rowMapper = (resultSet, rowNumber) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color")
    );

    public Long save(final String lineName, final String lineColor) {
        return insertAction.executeAndReturnKey(new MapSqlParameterSource("name", lineName).addValue("color", lineColor)).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from lines";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(Long id) {
        String sql = "select id, name, color from lines WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByName(final String lineName) {
        final String sql = "SELECT id, name, color FROM lines WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, lineName));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from lines where id = ?", id);
    }
}
