package subway.persistence.dao;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.LineEntity;

@Repository
public class LineDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<LineEntity> rowMapper = (resultSet, rowNumber) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("upward_terminus"),
            resultSet.getString("downward_terminus")
    );

    public LineDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("line")
                .usingColumns("name", "upward_terminus", "downward_terminus")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(LineEntity lineEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(lineEntity);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public LineEntity findById(Long id) {
        try {
            String sql = "SELECT id, name, upward_terminus, downward_terminus FROM line WHERE id=:id";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
            return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException(String.format(
                    "DB에서 ID에 해당하는 Line을 조회할 수 없습니다. " +
                            "(입력한 ID : %d)", id));
        }
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name, upward_terminus, downward_terminus FROM line";
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM line WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }

    public void update(LineEntity lineEntity) {
        String sql = "UPDATE line SET name=:name, upward_terminus=:upwardTerminus, downward_terminus=:downwardTerminus WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", lineEntity.getName())
                .addValue("upwardTerminus", lineEntity.getUpwardTerminus())
                .addValue("downwardTerminus", lineEntity.getDownwardTerminus())
                .addValue("id", lineEntity.getId());
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
    }
}
