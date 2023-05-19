package subway.adapter.line.out;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

class InterStationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<InterStationEntity> rowMapper = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final Long lineId = rs.getLong("line_id");
        final Long startStationId = rs.getLong("up_station_id");
        final Long endStationId = rs.getLong("down_station_id");
        final int distance = rs.getInt("distance");
        return new InterStationEntity(id, lineId, startStationId, endStationId, distance);
    };

    InterStationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("INTERSTATION")
                .usingColumns("line_id", "up_station_id", "down_station_id", "distance")
                .usingGeneratedKeyColumns("id");
    }

    public List<InterStationEntity> findAllByLineId(final Long id) {
        return jdbcTemplate.query(
                "select id, line_id, up_station_id, down_station_id, distance from INTERSTATION where line_id = ?",
                rowMapper, id);
    }

    public void insertAll(final List<InterStationEntity> interStationEntities) {
        final BeanPropertySqlParameterSource[] beanPropertySqlParameterSources = interStationEntities.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);

        simpleJdbcInsert.executeBatch(beanPropertySqlParameterSources);
    }

    public void deleteAllByLineId(final long id) {
        jdbcTemplate.update("delete from INTERSTATION where line_id = ?", id);
    }
}
