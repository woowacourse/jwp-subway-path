package subway.domain.lineDetail.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.lineDetail.entity.LineDetailEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDetailDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineDetailEntity> rowMapper = (rs, rowNum) ->
            new LineDetailEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDetailDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineDetailEntity insert(final LineDetailEntity lineDetailEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", lineDetailEntity.getId());
        params.put("name", lineDetailEntity.getName());
        params.put("color", lineDetailEntity.getColor());

        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineDetailEntity(lineId, lineDetailEntity.getName(), lineDetailEntity.getColor());
    }

    public List<LineDetailEntity> findAll() {
        final String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineDetailEntity findById(final Long id) {
        final String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Optional<LineDetailEntity> findByName(final String name) {
        try {
            final String sql = "select id, name, color from LINE WHERE name = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final LineDetailEntity newLineDetailEntity) {
        final String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLineDetailEntity.getName(), newLineDetailEntity.getColor(), newLineDetailEntity.getId()});
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
