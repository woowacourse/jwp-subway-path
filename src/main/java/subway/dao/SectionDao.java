package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

import static subway.dao.support.SqlHelper.sqlHelper;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<SectionEntity> sectionRowMapper = (rs, rn) -> new SectionEntity(
            rs.getLong("id"),
            rs.getInt("distance"),
            rs.getBoolean("is_start"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getLong("line_id")
    );

    public Long insert(
            final Long upStationId,
            final Long downStationId,
            final Long lineId,
            final boolean isStart,
            final Integer distance
    ) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("up_station_id", upStationId)
                .addValue("down_station_id", downStationId)
                .addValue("line_id", lineId)
                .addValue("is_start", isStart)
                .addValue("distance", distance)
        ).longValue();
    }

    public Long insert(final SectionEntity sectionEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("up_station_id", sectionEntity.getUpStationId())
                .addValue("down_station_id", sectionEntity.getDownStationId())
                .addValue("line_id", sectionEntity.getLineId())
                .addValue("is_start", sectionEntity.getStart())
                .addValue("distance", sectionEntity.getDistance())
        ).longValue();
    }

    public void insertBatch(final List<SectionEntity> sectionEntities) {
        final String sql = sqlHelper()
                .insert().table("section(up_station_id, down_station_id, line_id, is_start, distance)")
                .values("?, ?, ?, ?, ?")
                .toString();

        jdbcTemplate.batchUpdate(sql, sectionEntities, sectionEntities.size(),
                (ps, section) -> {
                    ps.setLong(1, section.getUpStationId());
                    ps.setLong(2, section.getDownStationId());
                    ps.setLong(3, section.getLineId());
                    ps.setBoolean(4, section.getStart());
                    ps.setInt(5, section.getDistance());
                });
    }

    public Optional<SectionEntity> findBySectionId(final Long sectionId) {
        final String sql = sqlHelper()
                .select().columns("id, distance, is_start, up_station_id, down_station_id, line_id")
                .from().table("section")
                .where().condition("id = ?")
                .toString();

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, sectionRowMapper, sectionId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<SectionEntity> findAllByLineId(final Long lineId) {
        final String sql = sqlHelper()
                .select().columns("id, distance, is_start, up_station_id, down_station_id, line_id")
                .from().table("section")
                .where().condition("line_id = ?")
                .toString();

        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    public void deleteBySectionId(final Long sectionId) {
        final String sql = sqlHelper()
                .delete()
                .from().table("section")
                .where().condition("id = ?")
                .toString();

        jdbcTemplate.update(sql, sectionId);
    }

    public void deleteByLineId(final Long lineId) {
        final String sql = sqlHelper()
                .delete()
                .from().table("section")
                .where().condition("line_id = ?")
                .toString();

        jdbcTemplate.update(sql, lineId);
    }
}
