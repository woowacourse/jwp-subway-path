package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.persistence.entity.LineStationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class LineStationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineStationEntity> rowMapper = (rs, rowNum) ->
            LineStationEntity.of(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("station_id")
            );

    public LineStationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line_station")
                .usingGeneratedKeyColumns("id");
    }

    public LineStationEntity insert(final LineStationEntity lineStationEntity) {
        final MapSqlParameterSource insertParameters = new MapSqlParameterSource()
                .addValue("line_id", lineStationEntity.getLineId())
                .addValue("station_id", lineStationEntity.getStationId());

        final long lineStationId = insertAction.executeAndReturnKey(insertParameters).longValue();
        return LineStationEntity.of(lineStationId, lineStationEntity.getLineId(), lineStationEntity.getStationId());
    }

    public List<LineStationEntity> findAll() {
        final String sql = "SELECT id, line_id, station_id FROM line_station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineStationEntity> findById(final Long id) {
        final String sql = "SELECT id, line_id, station_id FROM line_station WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findAny();
    }

    public int deleteById(final Long id) {
        final String sql = "DELETE FROM line_station WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
