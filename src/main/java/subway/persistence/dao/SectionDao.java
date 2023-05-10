package subway.persistence.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.SectionEntity;

@Component
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            SectionEntity.Builder
                    .builder()
                    .id(rs.getLong("id"))
                    .lineId(rs.getLong("line_id"))
                    .upStationId(rs.getLong("up_station_id"))
                    .downStationId(rs.getLong("down_station_id"))
                    .distance(rs.getInt("distance"))
                    .build();

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        final MapSqlParameterSource insertParameters = new MapSqlParameterSource()
                .addValue("line_id", sectionEntity.getLineId())
                .addValue("up_station_id", sectionEntity.getUpStationId())
                .addValue("down_station_id", sectionEntity.getDownStationId())
                .addValue("distance", sectionEntity.getDistance());
        final long sectionId = insertAction.executeAndReturnKey(insertParameters).longValue();

        return SectionEntity.Builder
                .builder()
                .id(sectionId)
                .lineId(sectionEntity.getLineId())
                .upStationId(sectionEntity.getUpStationId())
                .downStationId(sectionEntity.getDownStationId())
                .distance(sectionEntity.getDistance())
                .build();
    }

    public void insertAll(final List<SectionEntity> sectionEntities) {
        final MapSqlParameterSource[] insertParameters = sectionEntities.stream()
                .map(sectionEntity -> new MapSqlParameterSource()
                        .addValue("line_id", sectionEntity.getLineId())
                        .addValue("up_station_id", sectionEntity.getUpStationId())
                        .addValue("down_station_id", sectionEntity.getDownStationId())
                        .addValue("distance", sectionEntity.getDistance()))
                .toArray(MapSqlParameterSource[]::new);

        insertAction.executeBatch(insertParameters);
    }

    public List<SectionEntity> findAll() {
        final String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<SectionEntity> findAllByLineId(final Long lineId) {
        final String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public Optional<SectionEntity> findById(final Long id) {
        final String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section WHERE id = ?";

        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findAny();
    }

    public List<SectionEntity> findAllByStationId(final Long stationId) {
        final String sql = "SELECT id FROM section WHERE up_station_id = ? OR down_station_id = ?";

        return jdbcTemplate.query(sql, rowMapper, stationId, stationId);
    }

    public int deleteById(final Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    public int deleteByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";

        return jdbcTemplate.update(sql, lineId);
    }
}
