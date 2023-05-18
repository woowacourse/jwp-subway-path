package subway.line.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.line.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) -> new LineEntity
            .Builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .additionalFare(rs.getInt("additional_fare"))
            .build();

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineEntity.getName());
        params.put("additional_fare", lineEntity.getAdditionalFare());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Optional<LineEntity> findLineById(Long lineId) {
        String sql = "select id, name, additional_fare from LINE WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, lineId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, additional_fare from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findLineByName(String lineName) {
        String sql = "select id, name, additional_fare from LINE where name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, lineName));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteLineById(long id) {
        String sql = "delete from LINE where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
