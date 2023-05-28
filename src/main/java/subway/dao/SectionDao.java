package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.SectionEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SectionDao {

    private static final String TABLE_NAME = "section";
    private static final String ID = "id";
    private static final String LINE_ID = "line_id";
    private static final String UP_STATION_ID = "up_station_id";
    private static final String DOWN_STATION_ID = "down_station_id";
    private static final String DISTANCE = "distance";
    private static final String ALL_COLUMN = String.join(", ", ID, LINE_ID, UP_STATION_ID, DOWN_STATION_ID, DISTANCE);

    private static final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong(ID),
                    rs.getLong(LINE_ID),
                    rs.getLong(UP_STATION_ID),
                    rs.getLong(DOWN_STATION_ID),
                    rs.getInt(DISTANCE)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put(ID, sectionEntity.getId());
        params.put(LINE_ID, sectionEntity.getLineId());
        params.put(UP_STATION_ID, sectionEntity.getUpStationId());
        params.put(DOWN_STATION_ID, sectionEntity.getDownStationId());
        params.put(DISTANCE, sectionEntity.getDistance());

        long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity.getLineId(), sectionEntity.getUpStationId(), sectionEntity.getDownStationId(), sectionEntity.getDistance());
    }

    public List<SectionEntity> findAll() {
        String sql = "select " + ALL_COLUMN + " from " + TABLE_NAME;

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<SectionEntity> findByLineId(final long lineId) {
        String sql = "select " + ALL_COLUMN + " from " + TABLE_NAME + " where line_id = ?;";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public SectionEntity findById(final Long id) {
        String sql = "select " + ALL_COLUMN + " from " + TABLE_NAME + " WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from " + TABLE_NAME + " where id = ?", id);
    }

    public void deleteAllByLineId(final Long lineId) {
        jdbcTemplate.update("delete from " + TABLE_NAME + " where line_id = ?", lineId);
    }
}
