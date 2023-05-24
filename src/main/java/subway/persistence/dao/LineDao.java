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
            resultSet.getInt("surcharge"));

    public LineDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("line")
                .usingColumns("name", "surcharge")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(LineEntity lineEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(lineEntity);
        long savedId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return findById(savedId);
    }

    public LineEntity findById(Long id) {
        try {
            String sql = "SELECT id, name, surcharge FROM line WHERE id=:id";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
            return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException(String.format(
                    "DB에서 ID에 해당하는 Line을 조회할 수 없습니다. " +
                            "(입력한 ID : %d)", id));
        }
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name, surcharge FROM line";
        return namedParameterJdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity update(LineEntity lineEntity) {
        String sql = "UPDATE line SET name=:name, surcharge=:surcharge WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", lineEntity.getName())
                .addValue("surcharge", lineEntity.getSurcharge())
                .addValue("id", lineEntity.getId());
        namedParameterJdbcTemplate.update(sql, sqlParameterSource);
        return findById(lineEntity.getId());
    }
}
