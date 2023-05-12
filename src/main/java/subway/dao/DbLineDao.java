package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class DbLineDao implements LineDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertLine;

    public DbLineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
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
        return null;
    }

    @Override
    public LineEntity findById(final Long id) {
        return null;
    }

    @Override
    public void update(final LineEntity newLine) {

    }

    @Override
    public void deleteById(final Long id) {

    }
}
