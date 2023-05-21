package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;
import subway.exception.DuplicateLineException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<LineEntity> lineEntityRowMapper =
            (rs, rowNum) -> new LineEntity(rs.getLong("id"), rs.getString("name"));


    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(LineEntity lineEntity) {
        Optional<LineEntity> findLineEntity = findByLineName(lineEntity.getLineName());
        if (findLineEntity.isPresent()) {
            throw new DuplicateLineException();
        }
        Map<String, String> params = new HashMap<>();
        params.put("name", lineEntity.getLineName());
        Long insertedId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new LineEntity(insertedId, lineEntity.getLineName());
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name FROM line";
        return jdbcTemplate.query(sql, lineEntityRowMapper);
    }

    public LineEntity findById(Long id) {
        String sql = "SELECT id, name from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, lineEntityRowMapper, id);
    }

    public Optional<LineEntity> findByLineName(String lineName) {
        String sql = "SELECT id, name FROM line WHERE name = ?";
        List<LineEntity> lineEntities = jdbcTemplate.query(sql,
                lineEntityRowMapper, lineName
        );
        return lineEntities.stream().findAny();
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
