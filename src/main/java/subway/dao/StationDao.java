package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("line_id")
            );

    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public void insertAll(final List<StationEntity> stations) {
        final BeanPropertySqlParameterSource[] parameterSources = stations.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        jdbcInsert.executeBatch(parameterSources);
    }

    public StationEntity insert(final StationEntity stations) {
        final BeanPropertySqlParameterSource parameterSources = new BeanPropertySqlParameterSource(stations);
        Long id = jdbcInsert.executeAndReturnKey(parameterSources).longValue();
        return new StationEntity(id, stations.getName(), stations.getLineId());
    }

    public List<StationEntity> findAll() {
        String sql = "SELECT * FROM STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteByLineId(final Long lineId) {
        String sql = "DELETE FROM STATION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public List<StationEntity> findByLineId(final Long lineId) {
        String sql = "SELECT * FROM STATION where line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM STATION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
