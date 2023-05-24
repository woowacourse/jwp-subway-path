package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineH2Dao implements LineDao {

    public static final RowMapper<LineEntity> lineEntityRowMapper = (resultSet, rowNum) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color")
    );
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertLine;

    public LineH2Dao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertLine = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public LineEntity insert(final LineEntity line) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(line);

        final long id = insertLine.executeAndReturnKey(parameters).longValue();
        return new LineEntity(id, line.getName(), line.getColor());
    }

    @Override
    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, lineEntityRowMapper);
    }

    @Override
    public Optional<LineEntity> findById(final Long id) {
        final String sql = "SELECT * FROM line WHERE id = :id";
        final Map<String, Long> parameter = Map.of("id", id);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, parameter, lineEntityRowMapper));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
