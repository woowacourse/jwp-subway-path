package subway.dao.v2;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.domain.Distance;
import subway.domain.StationDomain;

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
            rs.getLong("down_station_id")
    );

    public Long insert(
            final Long upStationId,
            final Long downStationId,
            final boolean isStart,
            final Distance distance
    ) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("up_station_id", upStationId)
                .addValue("down_station_id", downStationId)
                .addValue("is_start", isStart)
                .addValue("distance", distance.getValue())
        ).longValue();
    }

    public Long insert(final SectionEntity sectionEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("up_station_id", sectionEntity.getUpStationId())
                .addValue("down_station_id", sectionEntity.getDownStationId())
                .addValue("is_start", sectionEntity.getStart())
                .addValue("distance", sectionEntity.getDistance())
        ).longValue();
    }

    public Optional<SectionEntity> findBySectionId(final Long sectionId) {
        final String sql = sqlHelper()
                .select()
                .columns("id, distance, is_start, up_station_id, down_station_id")
                .from()
                .table("SECTIONS")
                .where()
                .condition("id = ?")
                .toString();

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, sectionRowMapper, sectionId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private final RowMapper<StationDomain> stationRowMapper = (rs, rowNum) ->
            new StationDomain(
                    rs.getLong("id"),
                    rs.getString("name")
            );

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
