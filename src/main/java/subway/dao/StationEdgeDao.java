package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.StationEdgeEntity;

@Component
public class StationEdgeDao {

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

    public StationEdgeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station_edge")
                .usingGeneratedKeyColumns("id");
    }


    public Long insert(StationEdgeEntity stationEdgeEntity) {
        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(stationEdgeEntity);
        return insertAction.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public List<StationEdgeEntity> findByLineId(Long id) {
        String sql = "SELECT * FROM station_edge WHERE line_id = ?";

        return jdbcTemplate.query(sql, stationEdgeRowMapper, id);
    }

    public List<StationEdgeEntity> findAll() {
        String sql = "SELECT * FROM station_edge";

        return jdbcTemplate.query(sql, stationEdgeRowMapper);
    }

    public Optional<StationEdgeEntity> findByLineIdAndStationId(Long lineId, Long stationId) {
        String sql = "SELECT * FROM station_edge WHERE line_id = ? AND down_station_id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, stationEdgeRowMapper, lineId, stationId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(StationEdgeEntity stationEdgeEntity) {
        String sql = "UPDATE station_edge SET distance = ?, previous_station_edge_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, stationEdgeEntity.getDistance(), stationEdgeEntity.getPreviousStationEdgeId(),
                stationEdgeEntity.getId());
    }

    public void deleteByLineIdAndStationId(Long lineId, Long stationId) {
        String sql = "DELETE FROM station_edge WHERE line_id = ? AND down_station_id = ?";
        jdbcTemplate.update(sql, lineId, stationId);
    }
}
