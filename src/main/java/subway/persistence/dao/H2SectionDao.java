package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.Entity.SectionEntity;
import subway.persistence.NullChecker;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class H2SectionDao implements SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("upward_id"),
            rs.getLong("downward_id"),
            rs.getInt("distance")
    );

    public H2SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long insert(SectionEntity sectionEntity) {
        NullChecker.isNull(sectionEntity);
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(sectionEntity);
        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<SectionEntity> selectSectionsByLineId(long lineId) {
        NullChecker.isNull(lineId);
        String sql = "SELECT * FROM SECTION WHERE line_id=?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    @Override
    public List<SectionEntity> selectAllSections() {
        String sql = "SELECT * FROM SECTION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public long deleteAllByLineId(Long lineId) {
        NullChecker.isNull(lineId);
        String sql = "DELETE FROM SECTION WHERE line_id=?";
        return jdbcTemplate.update(sql, lineId);
    }

    @Override
    public void insertAll(List<SectionEntity> entities) {
        NullChecker.isNull(entities);
        List<SqlParameterSource> batchParams = new ArrayList<>();
        for (SectionEntity entity : entities) {
            SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(entity);
            batchParams.add(sqlParameterSource);
        }

        insertAction.executeBatch(batchParams.toArray(new SqlParameterSource[0]));
    }
}
