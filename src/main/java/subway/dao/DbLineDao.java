package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.entity.LineEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DbLineDao implements LineDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    private RowMapper<LineEntity> lineEntityRowMapper = (resultSet, rowNum) ->
            new LineEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getLong("station_id"),
                    resultSet.getLong("station_order")
            );

    public DbLineDao(JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public List<LineEntity> saveLineEntities(final List<LineEntity> lineEntities) {
        List<LineEntity> savedLineEntities = new ArrayList<>();
        for (final LineEntity lineEntity : lineEntities) {
            final SqlParameterSource parameters = new BeanPropertySqlParameterSource(lineEntity);
            final long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
            final LineEntity savedLineEntity = new LineEntity(id, lineEntity.getName(), lineEntity.getStationId(), lineEntity.getStationOrder());

            savedLineEntities.add(savedLineEntity);
        }

        return savedLineEntities;
    }

    @Override
    public List<LineEntity> findLine(Line line) {
        String sql = "select * from line where name = :name";
        final Map<String, String> parameter = Map.of("name", line.getName());
        return namedParameterJdbcTemplate.query(sql, parameter, lineEntityRowMapper);
    }

    @Override
    public void deleteAllStationsOfLine(Line line) {
        String sql = "delete from line where name = :name";
        final Map<String, String> parameters = Map.of("name", line.getName());
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public void findAllLines() {
    }
}
