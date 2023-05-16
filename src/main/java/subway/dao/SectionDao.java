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

    private RowMapper<SectionEntity> rowMapper = (resultSet, rowNumber) -> {
        return new SectionEntity(
                resultSet.getLong("id"),
                resultSet.getLong("up_station_id"),
                resultSet.getLong("down_station_id"),
                resultSet.getLong("line_id"),
                resultSet.getInt("distance")
        );
    };

    public Long save(final SectionEntity section) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("up_station_id", section.getUpStationId())
                .addValue("down_station_id", section.getDownStationId())
                .addValue("line_id", section.getLineId())
                .addValue("distance", section.getDistance())
        ).longValue();
    }

    public List<SectionEntity> findAll() {
        final String sql = "SELECT id, distance, up_station_id, down_station_id, line_id" +
                " FROM sections ";
        return jdbcTemplate.query(sql, rowMapper);
    }


    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT id, distance, up_station_id, down_station_id, line_id" +
                " FROM sections " +
                " WHERE line_id = ? ";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public Optional<SectionEntity> findDownSectionByStationIdAndLineId(final Long stationId, final Long lineId) {
        final String sql = "SELECT id, up_station_id, down_station_id, line_id, distance " +
                "FROM sections " +
                "WHERE up_station_id = ? " +
                "AND line_id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationId, lineId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<SectionEntity> findUpSectionByStationIdAndLineId(final Long stationId, final Long lineId) {
        final String sql = "SELECT id, up_station_id, down_station_id, line_id, distance, is_start " +
                "FROM sections " +
                "WHERE down_station_id = ? " +
                "AND line_id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationId, lineId));
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

    public Optional<SectionEntity> findByUpAndDown(final Long upStationId, final Long downStationId) {
        final String sql = "SELECT * " +
                "FROm sections " +
                "WHERE up_station_id = ? " +
                "AND down_station_id = ? ";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, upStationId, downStationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
