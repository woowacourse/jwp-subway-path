package subway.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;


@Component
public class SectionDao {

    private static final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
            new Section(
                    new Station(rs.getLong("up_station_id"), rs.getString("up_station_name")),
                    new Station(rs.getLong("down_station_id"), rs.getString("down_station_name")),
                    new Distance(rs.getInt("distance"))
            );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public List<Section> findSectionsByLineId(Long lineId) {
        String sql =
                "SELECT up.id as up_station_id, up.name as up_station_name, down.id as down_station_id, down.name as down_station_name, s.distance "
                        + "FROM SECTION AS s "
                        + "JOIN STATION AS up ON s.up_station_id = up.id "
                        + "JOIN STATION AS down ON s.down_station_id = down.id "
                        + "WHERE s.line_id = :line_id";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("line_id", lineId);

        return jdbcTemplate.query(sql, source, sectionRowMapper);
    }

    public Long insert(SectionEntity sectionEntity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        return insertAction.executeAndReturnKey(source).longValue();
    }

    public void delete(SectionEntity sectionEntity) {
        String sql = "DELETE FROM SECTION WHERE up_station_id = :upStationId AND down_station_id = :downStationId";

        SqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        jdbcTemplate.update(sql, source);
    }

}
