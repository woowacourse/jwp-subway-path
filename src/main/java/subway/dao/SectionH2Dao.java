package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.Map;

@Repository
public class SectionH2Dao implements SectionDao {

    private static final RowMapper<SectionEntity> SECTION_ENTITY_ROW_MAPPER = (resultSet, rowNum) -> new SectionEntity(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getLong("up_station_id"),
            resultSet.getLong("down_station_id"),
            resultSet.getInt("distance")
    );
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertSection;

    public SectionH2Dao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertSection = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public SectionEntity insert(final SectionEntity sectionEntity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(sectionEntity);
        final long id = insertSection.executeAndReturnKey(parameters).longValue();
        return new SectionEntity(
                id,
                sectionEntity.getLineId(),
                sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(),
                sectionEntity.getDistance());
    }

    @Override
    public List<SectionEntity> findAll() {
        final String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, SECTION_ENTITY_ROW_MAPPER);
    }

    @Override
    public void delete(final Long lineId, final Long upStationId, final Long downStationId) {
        final String sql = "DELETE FROM section WHERE line_id = :lineId AND up_station_id = :upStationId AND down_station_id = :downStationId";
        final Map<String, Long> parameters = Map.of(
                "lineId", lineId,
                "upStationId", upStationId,
                "downStationId", downStationId);
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public List<SectionEntity> findBy(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = :lineId";
        final Map<String, Long> parameter = Map.of("lineId", lineId);
        return namedParameterJdbcTemplate.query(sql, parameter, SECTION_ENTITY_ROW_MAPPER);
    }

    @Override
    public Long findStationIdBefore(final Long lineId, final Long stationId) {
        final String sql = "SELECT up_station_id FROM section WHERE line_id = :lineId AND down_station_id = :stationId";
        final Map<String, Long> parameters = Map.of(
                "lineId", lineId,
                "stationId", stationId
        );
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Long.class);
    }

    @Override
    public Long findStationIdAfter(final Long lineId, final Long stationId) {
        final String sql = "SELECT down_station_id FROM section WHERE line_id = :lineId AND up_station_id = :stationId";
        final Map<String, Long> parameters = Map.of(
                "lineId", lineId,
                "stationId", stationId
        );
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Long.class);
    }

    @Override
    public void deleteSectionsOf(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = :lineId";
        final Map<String, Long> parameter = Map.of("lineId", lineId);
        namedParameterJdbcTemplate.update(sql, parameter);
    }
}
