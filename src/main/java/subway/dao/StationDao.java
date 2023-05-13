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

    public List<StationEntity> findAll() {
        String sql = "SELECT * FROM STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteByLineId(final Long lineId) {
        String sql = "DELETE FROM STATION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
