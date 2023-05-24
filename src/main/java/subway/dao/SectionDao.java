package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;

import javax.sql.DataSource;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(SectionEntity section) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(section);
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void update(SectionEntity updateSection) {
        final String sql = "UPDATE section SET distance = ?, up_station_id = ?, down_station_id = ?";
        jdbcTemplate.update(sql, updateSection.getDistance(), updateSection.getUpStationId(), updateSection.getDownStationId());
    }

    public void delete(Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
