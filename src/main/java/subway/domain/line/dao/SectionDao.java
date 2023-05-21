package subway.domain.line.dao;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) -> new SectionEntity(rs.getLong("id"), rs.getLong("line_id"), rs.getLong("up_station_id"), rs.getLong("down_station_id"), rs.getInt("distance"));

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("section").usingGeneratedKeyColumns("id").usingColumns("line_id", "up_station_id", "down_station_id", "distance");
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("line_id", sectionEntity.getLineId());
        params.put("up_station_id", sectionEntity.getUpStationId());
        params.put("down_station_id", sectionEntity.getDownStationId());
        params.put("distance", sectionEntity.getDistance());

        final Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity);
    }

    public void deleteById(final Long id) {
        final String sql = "delete from SECTION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<SectionEntity> findByUpStationId(Long lineId, Long stationId) {
        try {
            final String sql = String.format("select id, line_id, up_station_id, down_station_id, distance from SECTION where line_id = ? and up_station_id = ?");
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<SectionEntity> findByDownStationId(Long lineId, Long stationId) {
        try {
            final String sql = String.format("select id, line_id, up_station_id, down_station_id, distance from SECTION where line_id = ? and down_station_id = ?");
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Boolean isEmpty(final Long lineId) {
        final String sql = String.format("select count(*) from SECTION where line_id = ?");
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, lineId);
        return count == 0;
    }

    public void update(final SectionEntity sectionEntity) {
        final String sql = "update section set line_id = ? , up_station_id = ?, down_station_id = ?, distance = ? where id = ?";
        jdbcTemplate.update(sql, sectionEntity.getLineId(), sectionEntity.getUpStationId(), sectionEntity.getDownStationId(), sectionEntity.getDistance(), sectionEntity.getId());
    }
}
