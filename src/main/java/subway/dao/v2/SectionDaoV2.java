package subway.dao.v2;

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
public class SectionDaoV2 {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDaoV2(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("sections")
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

    public Optional<SectionEntity> findBySectionId(final Long sectionId) {
        final String sql = sqlHelper()
                .select().columns("id, distance, is_start, up_station_id, down_station_id, line_id")
                .from().table("SECTIONS")
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
                .from().table("SECTIONS")
                .where().condition("line_id = ?")
                .toString();

        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    public void delete(final Long sectionId) {
        final String sql = sqlHelper()
                .delete()
                .from()
                .table("SECTIONS")
                .where()
                .condition("id = ?")
                .toString();

        jdbcTemplate.update(sql, sectionId);
    }
}
