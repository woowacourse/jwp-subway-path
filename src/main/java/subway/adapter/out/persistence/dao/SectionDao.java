package subway.adapter.out.persistence.dao;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.adapter.out.persistence.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );


    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(SectionEntity sectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", sectionEntity.getLineId());
        params.put("up_station_id", sectionEntity.getUpStationId());
        params.put("down_station_id", sectionEntity.getDownStationId());
        params.put("distance", sectionEntity.getDistance());

        Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity.getLineId(), sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(),
                sectionEntity.getDistance());
    }

    public void insertAll(List<SectionEntity> sectionEntities) {
        String sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) "
                + "VALUES (?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, sectionEntities, sectionEntities.size(),
                (PreparedStatement ps, SectionEntity sectionEntity) -> {
                    ps.setLong(1, sectionEntity.getLineId());
                    ps.setLong(2, sectionEntity.getUpStationId());
                    ps.setLong(3, sectionEntity.getDownStationId());
                    ps.setInt(4, sectionEntity.getDistance());
                });
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<SectionEntity> findByLineId(final long lineId) {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionEntity> findByStationId(final long stationId) {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section WHERE up_station_id = ? OR down_station_id = ?";
        return jdbcTemplate.query(sql, rowMapper, stationId, stationId);
    }

    public void update(SectionEntity newSectionEntity) {
        String sql = "UPDATE section SET line_id = ? , up_station_id = ?, down_station_id = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                newSectionEntity.getLineId(), newSectionEntity.getUpStationId(),
                newSectionEntity.getDownStationId(),
                newSectionEntity.getDistance(), newSectionEntity.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM section WHERE id = ?", id);
    }

    public void deleteAllByLineId(Long lineId) {
        jdbcTemplate.update("DELETE FROM section WHERE line_id = ?", lineId);
    }
}
