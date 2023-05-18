package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SectionDao {

    public static final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rn) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getInt("distance"),
            rs.getLong("previous_station_id"),
            rs.getLong("next_station_id")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final SectionEntity sectionEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(sectionEntity);
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * " +
                "FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        final String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, sectionEntityRowMapper);
    }

    public int deleteById(Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
