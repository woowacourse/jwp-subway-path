package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.dto.SectionDto;
import subway.domain.Distance;

import java.util.List;
import java.util.Optional;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("sections")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<SectionDto> sectionDtoRowMapper = (rs, rn) -> {
        return new SectionDto(
                rs.getLong("id"),
                rs.getLong("up_station_id"),
                rs.getLong("down_station_id"),
                rs.getLong("line_id"),
                rs.getInt("distance"),
                rs.getBoolean("is_start")
        );
    };

    public Long save(
            final Long upStationId,
            final Long downStationId,
            final Long lineId,
            final boolean isStart,
            final Distance distance
    ) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("up_station_id", upStationId)
                .addValue("down_station_id", downStationId)
                .addValue("line_id", lineId)
                .addValue("is_start", isStart)
                .addValue("distance", distance.getValue())
        ).longValue();
    }

    public List<SectionDto> findByLineId(final Long lineId) {
        final String sql = "SELECT id, distance, is_start, up_station_id, down_station_id, line_id" +
                " FROM sections " +
                " WHERE line_id = ? ";
        return jdbcTemplate.query(sql, sectionDtoRowMapper, lineId);
    }

    public Optional<SectionDto> findDownSectionByStationIdAndLineId(final Long stationId, final Long lineId) {
        final String sql = "SELECT id, up_station_id, down_station_id, line_id, distance, is_start " +
                "FROM sections " +
                "WHERE up_station_id = ? " +
                "AND line_id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, sectionDtoRowMapper, stationId, lineId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<SectionDto> findUpSectionByStationIdAndLineId(final Long stationId, final Long lineId) {
        final String sql = "SELECT id, up_station_id, down_station_id, line_id, distance, is_start " +
                "FROM sections " +
                "WHERE down_station_id = ? " +
                "AND line_id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, sectionDtoRowMapper, stationId, lineId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long findFirstStationIdByLineId(final Long lineId) {
        final String sql = "SELECT up_station_id " +
                "FROM sections " +
                "WHERE is_start = true " +
                "AND line_id = ? ";
        return jdbcTemplate.queryForObject(sql, Long.class, lineId);
    }

    public void delete(final Long sectionId) {
        final String sql = "DELETE FROM sections WHERE id = ?";
        jdbcTemplate.update(sql, sectionId);
    }
}
