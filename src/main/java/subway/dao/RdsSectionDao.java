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
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

@Repository
public class RdsSectionDao implements SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Section> rowMapper = (rs, rowNum) ->
            Section.builder()
                    .id(rs.getLong("id"))
                    .lineId(rs.getLong("line_id"))
                    .upStation(new Station(rs.getLong("up_station_id"), rs.getString("up_station_name")))
                    .downStation(new Station(rs.getLong("down_station_id"), rs.getString("down_station_name")))
                    .distance(new Distance(rs.getInt("distance")))
                    .build();

    public RdsSectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id")
                .usingColumns("line_id", "up_station_id", "down_station_id", "distance");
    }

    @Override
    public Section insert(final Section section) {
        final Map<String, Object> params = new HashMap<>();
        params.put("line_id", section.getLineId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance().getValue());

        final Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section);
    }

    @Override
    public Optional<Section> findById(final Long id) {
        final String sql = "SELECT sec.id," +
                "s1.id AS up_station_id," +
                "s1.name AS up_station_name," +
                "s2.id AS down_station_id," +
                "s2.name AS down_station_name," +
                "sec.line_id," +
                "sec.distance " +
                "FROM SECTION sec " +
                "JOIN STATION s1 ON sec.up_station_id = s1.id " +
                "JOIN STATION s2 ON sec.down_station_id = s2.id " +
                "WHERE sec.id = ?";
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
        final String sql = "SELECT sec.id," +
                "s1.id AS up_station_id," +
                "s1.name AS up_station_name," +
                "s2.id AS down_station_id," +
                "s2.name AS down_station_name," +
                "sec.line_id," +
                "sec.distance " +
                "FROM SECTION sec " +
                "JOIN STATION s1 ON sec.up_station_id = s1.id " +
                "JOIN STATION s2 ON sec.down_station_id = s2.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Section> findByLineId(final Long lineId) {
        final String sql = "SELECT sec.id," +
                "s1.id AS up_station_id," +
                "s1.name AS up_station_name," +
                "s2.id AS down_station_id," +
                "s2.name AS down_station_name," +
                "sec.line_id," +
                "sec.distance " +
                "FROM SECTION sec " +
                "JOIN STATION s1 ON sec.up_station_id = s1.id " +
                "JOIN STATION s2 ON sec.down_station_id = s2.id " +
                "WHERE sec.line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    @Override
    public Optional<Section> findUpSection(final Long lineId, final Long stationId) {
        try {
            final String sql = "SELECT sec.id," +
                    "s1.id AS up_station_id," +
                    "s1.name AS up_station_name," +
                    "s2.id AS down_station_id," +
                    "s2.name AS down_station_name," +
                    "sec.line_id," +
                    "sec.distance " +
                    "FROM SECTION sec " +
                    "JOIN STATION s1 ON sec.up_station_id = s1.id " +
                    "JOIN STATION s2 ON sec.down_station_id = s2.id " +
                    "WHERE sec.line_id = ? and sec.down_station_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Section> findDownSection(final Long lineId, final Long stationId) {
        try {
            final String sql = "SELECT sec.id," +
                    "s1.id AS up_station_id," +
                    "s1.name AS up_station_name," +
                    "s2.id AS down_station_id," +
                    "s2.name AS down_station_name," +
                    "sec.line_id," +
                    "sec.distance " +
                    "FROM SECTION sec " +
                    "JOIN STATION s1 ON sec.up_station_id = s1.id " +
                    "JOIN STATION s2 ON sec.down_station_id = s2.id " +
                    "WHERE sec.line_id = ? and sec.up_station_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
