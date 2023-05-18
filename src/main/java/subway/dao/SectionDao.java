package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<SectionEntity> sectionEntityRowMapper =
            (rs, rowNum) -> new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance"),
                    rs.getLong("line_id"));

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(SectionEntity sectionEntity) {
        Long upStationId = sectionEntity.getUpStationId();
        Long downStationId = sectionEntity.getDownStationId();
        int distance = sectionEntity.getDistance();
        Long lineId = sectionEntity.getLineId();

        Map<String, Object> params = new HashMap<>();
        params.put("up_station_id", upStationId);
        params.put("down_station_id", downStationId);
        params.put("distance", distance);
        params.put("line_id", lineId);

        Long insertedId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new SectionEntity(insertedId, upStationId, downStationId, distance, lineId);
    }

    public Optional<SectionEntity> findByStationIdsAndDistance(Long upStationId, Long downStationId, int distanceValue) {
        String sql = "SELECT id, up_station_id, down_station_id, distance, line_id " +
                "FROM section WHERE up_station_id = ? AND down_station_id = ? AND distance = ?";
        List<SectionEntity> findEntities = jdbcTemplate.query(sql, sectionEntityRowMapper, upStationId, downStationId, distanceValue);
        return findEntities.stream().findAny();
    }

    public Optional<SectionEntity> findByUpStationIdAndLindId(Long upStationId, Long lineId) {
        String sql = "SELECT id, up_station_id, down_station_id, distance, line_id " +
                "FROM section WHERE up_station_id = ? AND line_id = ?";

        List<SectionEntity> sectionEntities = jdbcTemplate.query(sql, sectionEntityRowMapper, upStationId, lineId);
        return sectionEntities.stream().findAny();
    }

    public Optional<SectionEntity> findByDownStationIdAndLindId(Long downStationId, Long lineId) {
        String sql = "SELECT id, up_station_id, down_station_id, distance, line_id " +
                "FROM section WHERE down_station_id = ? AND line_id = ?";

        List<SectionEntity> sectionEntities = jdbcTemplate.query(sql, sectionEntityRowMapper, downStationId, lineId);
        return sectionEntities.stream().findAny();
    }

    public void deleteBySectionId(Long id) {
        String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
