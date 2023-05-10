package subway.section.dao;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.section.domain.Direction;
import subway.section.domain.Section;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Section> rowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Section insert(final Section section) {
        final Map<String, Object> params = new HashMap<>();
        params.put("line_id", section.getLineId());
        params.put("up_station_id", section.getUpStationId());
        params.put("down_station_id", section.getDownStationId());
        params.put("distance", section.getDistance());

        final Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section);
    }

    public Optional<Section> findById(final Long id) {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(final Long id) {
        final String sql = "delete from SECTION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Section> findByLineId(final Long lineId) {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }


    public Optional<Section> findNeighborStation(final Long lineId, final Long baseId, final Direction direction) {
        try {
            if (direction == Direction.UP) {
                final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ? and down_station_id = ?";
                return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, baseId));
            }
            final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ? and up_station_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, baseId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
