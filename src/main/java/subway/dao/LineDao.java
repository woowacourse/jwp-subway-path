package subway.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert lineInsertAction;
    private final SimpleJdbcInsert interStationInsertAction;
    private final RowMapper<Optional<LineEntity>> optionalRowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String color = rs.getString("color");
        return Optional.of(new LineEntity(id, name, color, new ArrayList<>()));
    };

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String name = rs.getString("name");
        final String color = rs.getString("color");
        return new LineEntity(id, name, color, new ArrayList<>());
    };

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        lineInsertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingColumns("name", "color")
                .usingGeneratedKeyColumns("id");
        interStationInsertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("INTERSTATION")
                .usingColumns("line_id", "start_station_id", "end_station_id", "distance")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(final LineEntity lineEntity) {
        final Map<String, Object> lineParams = new HashMap<>();
        lineParams.put("name", lineEntity.getName());
        lineParams.put("color", lineEntity.getColor());
        final Long lineId = lineInsertAction.executeAndReturnKey(lineParams).longValue();

        @SuppressWarnings("unchecked") final Map<String, Object>[] interStationParams = lineEntity.getInterStationEntities()
                .stream()
                .map(interStationEntity -> {
                    final Map<String, Object> stationParam = new HashMap<>();
                    stationParam.put("line_id", lineId);
                    stationParam.put("start_station_id", interStationEntity.getFrontStationId());
                    stationParam.put("end_station_id", interStationEntity.getBackStationId());
                    stationParam.put("distance", interStationEntity.getDistance());
                    return stationParam;
                }).toArray(Map[]::new);
        interStationInsertAction.executeBatch(interStationParams);

        return findById(lineId).get();
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT line.id, line.name,line.color FROM LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(final Long id) {
        final String sql = "SELECT line.id, line.name,line.color FROM LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, optionalRowMapper, id);
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    public Optional<LineEntity> findByName(final String name) {
        final String sql = "SELECT line.id, line.name,line.color FROM LINE WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, optionalRowMapper, name);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
