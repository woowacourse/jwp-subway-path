package subway.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Section> rowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Section insert(final Section section) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(section);
        long id = insertAction.executeAndReturnKey(params).longValue();
        return new Section(id, section.getLineId(), section.getUpStationId(), section.getDownStationId(),
                section.getDistance());
    }

    public List<Section> findAll() {
        String sql = "select * from SECTION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void update(final Section section) {
        String sql = "update SECTION set up_station_id = ?, down_station_id = ?, distance = ? where id = ?";
        jdbcTemplate.update(sql, section.getUpStationId(), section.getDownStationId(), section.getDistance(),
                section.getId());
    }

    public void deleteById(final Long id) {
        String sql = "delete SECTION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteAllByLineId(final Long lineId) {
        String sql = "delete SECTION where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void deleteAll() {
        String sql = "delete SECTION";
        jdbcTemplate.update(sql);
    }
}
