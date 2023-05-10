package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("line_id")
            );

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity findById(final Long id) {
        final String sql = "SELECT * FROM station WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<StationEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM station WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public Long save(final StationEntity stationEntity) {
        return insertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(stationEntity)).longValue();
    }

    public StationEntity findByName(final String name) {
        final String sql = "SELECT * FROM station WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }
}
