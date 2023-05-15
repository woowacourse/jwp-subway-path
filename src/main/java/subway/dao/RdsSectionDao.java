package subway.dao;


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
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;

@Repository
public class RdsSectionDao implements SectionDao {

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

    private final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    new Station(rs.getLong("up_station_id"), rs.getString("up_station_name")),
                    new Station(rs.getLong("down_station_id"), rs.getString("down_station_name")),
                    new Distance(rs.getInt("distance"))
            );

    public RdsSectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public SectionEntity insert(final SectionEntity sectionEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("line_id", sectionEntity.getLineId());
        params.put("up_station_id", sectionEntity.getUpStationId());
        params.put("down_station_id", sectionEntity.getDownStationId());
        params.put("distance", sectionEntity.getDistance());

        final Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity);
    }

    @Override
    public Optional<SectionEntity> findById(final Long id) {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "delete from SECTION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Section> findAll() {
        final String sql = "SELECT s1.id AS up_station_id," +
                "s1.name AS up_station_name," +
                "s2.id AS down_station_id," +
                "s2.name AS down_station_name," +
                "sec.line_id," +
                "sec.distance " +
                "FROM SECTION sec " +
                "JOIN STATION s1 ON sec.up_station_id = s1.id " +
                "JOIN STATION s2 ON sec.down_station_id = s2.id";
        return jdbcTemplate.query(sql, sectionRowMapper);
    }

    @Override
    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    @Override
    public Optional<SectionEntity> findNeighborSection(final Long lineId, final Long baseId, final Direction direction) {
        if (direction == Direction.UP) {
            return findNeighborUpSection(lineId, baseId);
        }
        return findNeighborDownSection(lineId, baseId);
    }

    @Override
    public Optional<SectionEntity> findNeighborUpSection(final Long lineId, final Long stationId) {
        try {
            final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ? and down_station_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SectionEntity> findNeighborDownSection(final Long lineId, final Long stationId) {
        try {
            final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ? and up_station_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
