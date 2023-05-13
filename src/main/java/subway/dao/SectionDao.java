package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {

    private static final RowMapper<SectionEntity> ROW_MAPPER = (rs, count) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("upward_station_id"),
            rs.getString("upward_station_name"),
            rs.getLong("downward_station_id"),
            rs.getString("downward_station_name"),
            rs.getInt("distance")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingColumns("line_id", "upward_station_id", "downward_station_id", "distance")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity save(final SectionEntity sectionEntity) {
        final SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(sectionEntity);
        final Long sectionId = jdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        final String sql = "SELECT s.id AS id,"
                + " s.line_id AS line_id,"
                + " us.id AS upward_station_id,"
                + " us.name AS upward_station_name,"
                + " ds.id AS downward_station_id,"
                + " ds.name AS downward_station_name,"
                + " s.distance AS distance"
                + " FROM section s"
                + " JOIN station us ON s.upward_station_id = us.id"
                + " JOIN station ds ON s.downward_station_id = ds.id"
                + " WHERE s.id = ?";
        return jdbcTemplate.getJdbcOperations().queryForObject(sql, ROW_MAPPER, sectionId);
    }

    public void saveAll(final List<SectionEntity> sectionEntities) {
        final String sql = "INSERT INTO section (line_id, upward_station_id, downward_station_id, distance)"
                + " VALUES (:lineId, :upwardStationId, :downwardStationId, :distance)";
        jdbcTemplate.batchUpdate(sql, SqlParameterSourceUtils.createBatch(sectionEntities));
    }

    public List<SectionEntity> findAllByLineId(final Long lineId) {
        final String sql = "SELECT s.id AS id,"
                + " s.line_id AS line_id,"
                + " us.id AS upward_station_id,"
                + " us.name AS upward_station_name,"
                + " ds.id AS downward_station_id,"
                + " ds.name AS downward_station_name,"
                + " s.distance AS distance"
                + " FROM section s"
                + " JOIN station us ON s.upward_station_id = us.id"
                + " JOIN station ds ON s.downward_station_id = ds.id"
                + " WHERE s.line_id = ?"
                + " ORDER BY s.id";
        return jdbcTemplate.getJdbcOperations().query(sql, ROW_MAPPER, lineId);
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.getJdbcOperations().update(sql, lineId);
    }
}
