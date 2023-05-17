package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.domain.Station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
            new Section(
                    new Station(rs.getLong("up_station_id"),
                            rs.getString("up_station_name")),
                    new Station(rs.getLong("down_station_id"),
                            rs.getString("down_station_name")),
                    rs.getInt("distance")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final Long lineId, final Section section) {
        final Map<String, Object> params = new HashMap<>();
        params.put("line_id", lineId);
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("distance", section.getDistance());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<Section> findByLineId(final Long lineId) {
        final String sql = "SELECT se.up_station_id, " +
                "up_st.name AS up_station_name, " +
                "se.down_station_id, " +
                "down_st.name AS down_station_name, " +
                "se.distance " +
                "FROM section AS se " +
                "INNER JOIN station AS up_st ON se.up_station_id = up_st.id " +
                "INNER JOIN station AS down_st ON se.down_station_id = down_st.id " +
                "WHERE line_id = ?";

        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

}
