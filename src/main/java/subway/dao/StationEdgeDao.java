package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.StationEdgeEntity;

import java.util.List;
import java.util.Optional;

@Component
public class StationEdgeDao implements Dao<StationEdgeEntity> {

    private static final RowMapper<StationEdgeEntity> stationEdgeRowMapper = (rs, i) -> {
        Long previousStationEdgeId = rs.getLong("previous_station_edge_id");
        if (previousStationEdgeId == 0) {
            previousStationEdgeId = null;
        }
        return new StationEdgeEntity(
                rs.getLong("id"),
                rs.getLong("line_id"),
                rs.getLong("down_station_id"),
                rs.getInt("distance"),
                previousStationEdgeId
        );
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationEdgeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station_edge")
                .usingGeneratedKeyColumns("id");
    }


    @Override
    public Long insert(final StationEdgeEntity stationEdgeEntity) {
        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(stationEdgeEntity);
        return insertAction.executeAndReturnKey(sqlParameterSource).longValue();
    }

    @Override
    public Optional<StationEdgeEntity> findById(final Long id) {
        final String sql = "SELECT * from station where id = ?";
        return findInOptional(jdbcTemplate, sql, stationEdgeRowMapper, id);
    }

    @Override
    public List<StationEdgeEntity> findAll() {
        final String sql = "SELECT * FROM station_edge";

        return jdbcTemplate.query(sql, stationEdgeRowMapper);
    }

    @Override
    public void update(final StationEdgeEntity stationEdgeEntity) {
        final String sql = "UPDATE station_edge SET distance = ?, previous_station_edge_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, stationEdgeEntity.getDistance(), stationEdgeEntity.getPreviousStationEdgeId(),
                stationEdgeEntity.getId());
    }

    @Override
    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM station_edge WHERE id = ?", id);
    }

    public List<StationEdgeEntity> findByLineId(final Long id) {
        final String sql = "SELECT * FROM station_edge WHERE line_id = ?";

        return jdbcTemplate.query(sql, stationEdgeRowMapper, id);
    }

    public Optional<StationEdgeEntity> findByLineIdAndStationId(final Long lineId, final Long stationId) {
        final String sql = "SELECT * FROM station_edge WHERE line_id = ? AND down_station_id = ?";
        return findInOptional(jdbcTemplate, sql, stationEdgeRowMapper, lineId, stationId);
    }

    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        final String sql = "DELETE FROM station_edge WHERE line_id = ? AND down_station_id = ?";
        jdbcTemplate.update(sql, lineId, stationId);
    }

    public void deleteByLineId(final Long lineId) {
        final String sql = "DELETE FROM station_edge WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
