package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.Entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2LineDao implements LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            LineEntity.of(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("extra_fare")
            );

    public H2LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public LineEntity insert(final LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());
        params.put("extra_fare", lineEntity.getExtraFare());

        long lineId = insertAction.executeAndReturnKey(params).longValue();
        return LineEntity.of(lineId, lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFare());
    }

    @Override
    public List<LineEntity> findAll() {
        String sql = "select * from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<LineEntity> findById(final long id) {
        try {
            String sql = "select * from LINE WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public int update(final LineEntity lineEntity) {
        String sql = "update LINE set name = ?, color = ?, extra_fare = ? where id = ?";
        return jdbcTemplate.update(sql, lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFare(), lineEntity.getId());
    }

    @Override
    public int deleteById(final long id) {
        return jdbcTemplate.update("delete from Line where id = ?", id);
    }

    @Override
    public int countByName(final String name) {
        String sql = "SELECT COUNT(*) AS count " +
                "FROM LINE " +
                "WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, name);
    }

    @Override
    public int countByColor(final String color) {
        String sql = "SELECT COUNT(*) AS count " +
                "FROM LINE " +
                "WHERE color = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, color);
    }
}
