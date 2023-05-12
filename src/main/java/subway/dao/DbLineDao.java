package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class DbLineDao implements LineDao {

    public static final RowMapper<LineEntity> lineEntityRowMapper = (resultSet, rowNum) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color")
    );
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertLine;

    public DbLineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertLine = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public LineEntity save(final LineEntity line) {
        final String name = line.getName();
        final String color = line.getColor();

        final Map<String, String> parameters = Map.of(
                "name", name,
                "color", color);

        final long id = insertLine.executeAndReturnKey(parameters).longValue();
        return new LineEntity(id, name, color);
    }

    @Override
    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, lineEntityRowMapper);
    }

    @Override
    public LineEntity findById(final Long id) {
        final String sql = "SELECT * FROM line WHERE id = :id";
        final Map<String, Long> parameter = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameter, lineEntityRowMapper);
    }

    @Override
    public void update(final LineEntity newLine) {

    }

    @Override
    public void deleteById(final Long id) {
    }
}
