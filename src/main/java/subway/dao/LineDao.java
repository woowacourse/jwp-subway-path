package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
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

    private RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    new LineName(rs.getString("name")),
                    new LineColor(rs.getString("color")),
                    null,
                    null,
                    Collections.EMPTY_LIST
            );

    public Long save(final String lineName, final String lineColor) {
        return insertAction.executeAndReturnKey(new MapSqlParameterSource("name", lineName).addValue("color", lineColor)).longValue();
    }

    public List<Line> findAll() {
        String sql = "select id, name, color from lines";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Line> findById(Long id) {
        String sql = "select id, name, color from lines WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Line> findByName(final String lineName) {
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
