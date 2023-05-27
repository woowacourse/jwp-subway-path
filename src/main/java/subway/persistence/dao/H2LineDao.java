package subway.persistence.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.Entity.LineEntity;
import subway.domain.line.Line;
import subway.persistence.NullChecker;

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
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("extra_fare")
            );

    public H2LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public LineEntity insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("extra_fare", line.getExtraFare());

        long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, line.getName(), line.getColor(), line.getExtraFare());
    }

    @Override
    public List<LineEntity> findAll() {
        String sql = "select id, name, color, extra_fare from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<LineEntity> findById(Long id) {
        NullChecker.isNull(id);
        String sql = "select id, name, color, extra_fare from LINE WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void update(LineEntity lineEntity) {
        NullChecker.isNull(lineEntity);
        String sql = "update LINE set name = ?, color = ?, extra_fare = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFare(), lineEntity.getId()});
    }

    @Override
    public void deleteById(Long id) {
        NullChecker.isNull(id);
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
