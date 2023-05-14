package subway.section.persistence;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.section.dto.SectionStationDto;

@Repository
public class SectionDao {

    private static final RowMapper<SectionStationDto> SECTION_STATION_DTO_ROW_MAPPER = (rs, rowNum) -> new SectionStationDto(
        rs.getLong("section_id"),
        rs.getLong("up_station_id"),
        rs.getString("up_station_name"),
        rs.getLong("down_station_id"),
        rs.getString("down_station_name"),
        rs.getInt("distance")
    );

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("SECTION")
            .usingColumns("line_id", "up_station_id", "down_station_id", "distance")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(final SectionEntity sectionEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(sectionEntity))
            .longValue();
    }

    public List<SectionStationDto> findAllByLineId(final Long lineId) {
        final String query =
            "SELECT sec.id AS section_id, s1.id AS up_station_id, s1.stationName AS up_station_name, s2.id AS "
                + "down_station_id, s2.stationName AS "
                + "down_station_name, sec.distance AS distance "
                + "FROM SECTION sec "
                + "JOIN STATION s1 ON sec.up_station_id = s1.id "
                + "JOIN STATION s2 ON sec.down_station_id = s2.id "
                + "WHERE line_id = ?";

        return jdbcTemplate.query(query, SECTION_STATION_DTO_ROW_MAPPER, lineId);
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE FROM SECTION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
