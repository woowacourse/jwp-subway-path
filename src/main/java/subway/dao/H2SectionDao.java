package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;
import subway.exception.ApiNoSuchResourceException;
import subway.exception.NoSuchLineException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2SectionDao implements SectionDao {

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

    public H2SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public SectionEntity insert(final SectionEntity sectionEntity) {
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

    @Override
    public void insertAll(final List<SectionEntity> sectionEntities) {
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

    @Override
    public List<SectionEntity> findAll() {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<SectionEntity> findById(final Long id) {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    @Override
    public List<SectionEntity> findAllByLineId(final long lineId) {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    @Override
    public void update(final SectionEntity newSectionEntity) {
        if (!existsById(newSectionEntity.getId())) {
            throw new NoSuchLineException(newSectionEntity.getId());
        }
        String sql = "UPDATE section SET line_id = ?, up_station_id = ?, down_station_id = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                newSectionEntity.getLineId(), newSectionEntity.getUpStationId(),
                newSectionEntity.getDownStationId(),
                newSectionEntity.getDistance(), newSectionEntity.getId());
    }

    @Override
    public void deleteById(final Long id) {
        if (!existsById(id)) {
            throw new ApiNoSuchResourceException("존재하지 않는 구간입니다 id : " + id);
        }
        jdbcTemplate.update("DELETE FROM section WHERE id = ?", id);
    }

    @Override
    public void deleteAllByLineId(Long lineId) {
        jdbcTemplate.update("DELETE FROM section WHERE line_id = ?", lineId);
    }

    private boolean existsById(final Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM section WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Boolean.class);
    }
}
