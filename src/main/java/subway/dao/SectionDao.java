package subway.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.dao.vo.SectionStationMapper;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionDao {

    private static final RowMapper<SectionStationMapper> sectionStationRowMapper = (rs, rowNum) ->
            new SectionStationMapper(
                    rs.getLong("id"),
                    rs.getLong("up_station_id"),
                    rs.getString("up_station_name"),
                    rs.getLong("down_station_id"),
                    rs.getString("down_station_name"),
                    rs.getInt("distance"));
    private static final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public List<SectionStationMapper> findAll() {
        String sql = "SELECT s.id, up.id as up_station_id, up.name as up_station_name, down.id as down_station_id, down.name as down_station_name, s.distance "
                + "FROM SECTION AS s "
                + "JOIN STATION AS up ON s.up_station_id = up.id "
                + "JOIN STATION AS down ON s.down_station_id = down.id ";

        return jdbcTemplate.query(sql, sectionStationRowMapper);
    }

    public List<SectionStationMapper> findSectionsByLineId(Long lineId) {
        String sql = "SELECT s.id, up.id as up_station_id, up.name as up_station_name, down.id as down_station_id, down.name as down_station_name, s.distance "
                + "FROM SECTION AS s "
                + "JOIN STATION AS up ON s.up_station_id = up.id "
                + "JOIN STATION AS down ON s.down_station_id = down.id "
                + "WHERE s.line_id = :line_id";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("line_id", lineId);

        return jdbcTemplate.query(sql, source, sectionStationRowMapper);
    }

    public Optional<List<SectionEntity>> findByLineId(Long lineId) {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM SECTION WHERE line_id = :line_id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("line_id", lineId);
        try {
            return Optional.of(jdbcTemplate.query(sql, source, sectionEntityRowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<SectionEntity> findByUpStationId(Long upStationId, Long lineId) {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM SECTION WHERE line_id = :line_id AND up_station_id = :up_station_id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("up_station_id", upStationId)
                .addValue("line_id", lineId);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, sectionEntityRowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<SectionEntity> findByDownStationId(Long downStationId, Long lineId) {
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM SECTION WHERE line_id = :line_id AND down_station_id = :down_station_id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("down_station_id", downStationId)
                .addValue("line_id", lineId);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, sectionEntityRowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void insert(SectionEntity sectionEntity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        insertAction.execute(source);
    }

    public void insertAll(List<SectionEntity> sectionEntities) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(sectionEntities.toArray());
        jdbcTemplate.batchUpdate(
                "INSERT INTO SECTION (line_id, up_station_id, down_station_id, distance) " +
                        "VALUES (:lineId, :upStationId, :downStationId, :distance)",
                batch
        );
    }

    public void updateByUpStationId(SectionEntity sectionEntity) {
        String sql = "UPDATE SECTION SET down_station_id = :downStationId, distance = :distance "
                + "WHERE id = :id";

        SqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        jdbcTemplate.update(sql, source);
    }

    public void updateByDownStationId(SectionEntity sectionEntity) {
        String sql = "UPDATE SECTION SET up_station_id = :upStationId, distance = :distance "
                + "WHERE line_id = :lineId AND down_station_id = :downStationId";

        SqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        jdbcTemplate.update(sql, source);
    }

    public void delete(SectionEntity sectionEntity) {
        String sql = "DELETE FROM SECTION WHERE id = :id";

        SqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        jdbcTemplate.update(sql, source);
    }

    public void deleteAll(List<SectionEntity> sectionEntities) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(sectionEntities.toArray());
        jdbcTemplate.batchUpdate(
                "DELETE FROM SECTION WHERE id = :id",
                batch
        );
    }
}
