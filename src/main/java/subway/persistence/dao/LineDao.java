package subway.persistence.dao;

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
        //TODO EmptyResultDataAccessException 처리
        String sql = "SELECT id, name, upward_terminus, downward_terminus FROM line WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);
    }
}
