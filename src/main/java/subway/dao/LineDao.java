package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<LineEntity> lineEntityRowMapper =
            (rs, rowNum) -> new LineEntity(rs.getLong("id"), rs.getString("name"));


    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public LineEntity insert(LineEntity lineEntity) {
        Optional<LineEntity> findLineEntity = findByLineName(lineEntity.getLineName());
        if (findLineEntity.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 노선입니다.");
        }
        String sql = "INSERT INTO line (name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, lineEntity.getLineName());
            return preparedStatement;
        }, keyHolder);

        long insertedId = keyHolder.getKey().longValue();
        return new LineEntity(insertedId, lineEntity.getLineName());
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name FROM line";
        return jdbcTemplate.query(sql, lineEntityRowMapper);
    }

    public LineEntity findById(Long id) {
        String sql = "SELECT id, line_name from LINE WHERE id = ?";
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
