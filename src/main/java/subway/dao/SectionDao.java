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

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rn) -> new SectionEntity.Builder()
            .id(rs.getLong("id"))
            .lineId(rs.getLong("line_id"))
            .previousStationId(rs.getLong("previous_station_id"))
            .nextStationId(rs.getLong("next_station_id"))
            .distance(rs.getInt("distance")).build();

    public SectionDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    // TODO: 없는 호선이나 역을 입력한 경우 예외 처리 해야함
    public Long insert(final SectionEntity sectionEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(sectionEntity);
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<SectionEntity> findByLineIdAndPreviousStationId(final Long lineId, final Long previousStationId) {
        final String sql = "SELECT * FROM section WHERE line_id = ? AND previous_station_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId, previousStationId);
    }
}
