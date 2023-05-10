package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.domain.Station;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private static final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("Line")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(LineEntity lineEntity) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, lineEntity.getName(), lineEntity.getColor(), lineEntity.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from LINE where id = ?", id);
    }

    public void updateStartStationById(Long id, Station station) {
        String sql = "UPDATE LINE SET start_station_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, station.getName(), id);

    }
}
