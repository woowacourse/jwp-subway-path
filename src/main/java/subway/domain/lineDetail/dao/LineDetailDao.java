package subway.domain.lineDetail.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.lineDetail.domain.LineDetail;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDetailDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineDetail> rowMapper = (rs, rowNum) ->
            new LineDetail(
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

    public LineDetail insert(final LineDetail lineDetail) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", lineDetail.getId());
        params.put("name", lineDetail.getName());
        params.put("color", lineDetail.getColor());

        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineDetail(lineId, lineDetail.getName(), lineDetail.getColor());
    }

    public List<LineDetail> findAll() {
        final String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineDetail findById(final Long id) {
        final String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Optional<LineDetail> findByName(final String name) {
        try {
            final String sql = "select id, name, color from LINE WHERE name = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final LineDetail newLineDetail) {
        final String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLineDetail.getName(), newLineDetail.getColor(), newLineDetail.getId()});
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
