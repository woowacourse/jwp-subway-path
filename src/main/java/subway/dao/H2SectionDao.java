package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.Entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class H2SectionDao implements SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("upward_id"),
                    rs.getLong("downward_id"),
                    rs.getInt("distance"),
                    rs.getLong("line_id")
            );

    public H2SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long insert(SectionEntity sectionEntity) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(sectionEntity);
        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<SectionEntity> selectAll() {
        String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public long deleteById(long id) {
        String sql = "DELETE FROM section WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public SectionEntity selectByStationIdsAndLineId(long upwardId, long downwardId, long lineId) {
        String sql = "SELECT * FROM SECTION WHERE upward_id=? AND downward_id=? AND line_id=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, upwardId, downwardId, lineId);
    }

    @Override
    public SectionEntity selectSectionAtLast(long stationId, long lineId) {
        String sql = "SELECT * FROM SECTION WHERE (upward_id=? OR downward_id=?) AND line_id=? AND distance=?";
        return jdbcTemplate.queryForObject(sql, rowMapper, stationId, stationId, lineId, 0);
    }

    @Override
    public List<SectionEntity> selectSectionsByStationIdAndLineId(long stationId, long lineId) {
        String sql = "SELECT * FROM SECTION WHERE (upward_id=? OR downward_id=?) AND line_id=?";
        return jdbcTemplate.query(sql, rowMapper, stationId, stationId, lineId);
    }

}
