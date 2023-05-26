package subway.persistence.dao;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.dao.entity.LineEntity;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(LineEntity line) {
        try {
            BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(line);
            long lineId = insertAction.executeAndReturnKey(parameters).longValue();
            return new LineEntity(lineId, line.getName(), line.getColor());
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("해당 노선은 이미 존재합니다.");
        }
    }

    public Optional<LineEntity> findLineById(long lineId) {
        String sql = "SELECT * FROM line WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId).stream()
                .findAny();
    }
}
