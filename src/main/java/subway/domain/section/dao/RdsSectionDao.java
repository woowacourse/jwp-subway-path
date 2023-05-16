package subway.domain.section.dao;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.section.domain.Direction;
import subway.domain.section.domain.Section;
import subway.domain.section.entity.SectionEntity;
import subway.domain.station.domain.Station;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                    rs.getInt("distance")
            );

    public RdsSectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id")
                .usingColumns("line_id", "up_station_id", "down_station_id", "distance");
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
    public void deleteById(final Long id) {
        final String sql = "delete from SECTION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    @Override
    public List<Section> findSectionsByLineId(final Long lineId) {
        final String sql = "SELECT s1.id AS up_station_id," +
                "s1.name AS up_station_name," +
                "s2.id AS down_station_id," +
                "s2.name AS down_station_name," +
                "sec.line_id," +
                "sec.distance " +
                "FROM SECTION sec " +
                "JOIN STATION s1 ON sec.up_station_id = s1.id " +
                "JOIN STATION s2 ON sec.down_station_id = s2.id " +
                "WHERE line_id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    @Override
    public Optional<SectionEntity> findNeighborSection(final Long lineId, final Long stationId, final Direction direction) {
        try {
            final String sql = String.format("select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE line_id = ? and %s_station_id = ?", direction.reverseDirectionName());
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
