package subway.persistence.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.LineEntity;

@Component
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            LineEntity.of(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(final LineEntity lineEntity) {
        final MapSqlParameterSource insertParameters = new MapSqlParameterSource()
                .addValue("name", lineEntity.getName())
                .addValue("color", lineEntity.getColor());

        final Long lineId = insertAction.executeAndReturnKey(insertParameters).longValue();
        return LineEntity.of(lineId, lineEntity.getName(), lineEntity.getColor());
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT id, name, color FROM line";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(final Long id) {
        final String sql = "SELECT id, name, color FROM line WHERE id = ?";

        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findAny();
    }

    public boolean existsByName(final String name) {
        final String sql = "SELECT id, name, color FROM line WHERE name = ?";

        return jdbcTemplate.query(sql, rowMapper, name)
                .stream()
                .findAny()
                .isPresent();
    }

    public int deleteById(final Long id) {
        final String sql = "DELETE FROM line WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }
}
