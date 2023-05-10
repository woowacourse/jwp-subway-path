package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("start_station_id"),
                    rs.getLong("end_station_id"),
                    rs.getLong("line_id"),
                    rs.getInt("distance")
            );

    private final RowMapper<SectionInformationDto> rowDetailMapper = (rs, rowNum) ->
            new SectionInformationDto(
                    rs.getString("start_station.name"),
                    rs.getString("end_station.name"),
                    rs.getInt("section.distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(final SectionEntity sectionEntity) {
        return insertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(sectionEntity)).longValue();
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteByStationsId(final Long startStationId, final Long endStationId) {
        final String sql = "DELETE FROM section WHERE start_station_id = ? AND end_station_id = ?";
        jdbcTemplate.update(sql, startStationId, endStationId);
    }

    public List<SectionInformationDto> findDetailsByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section "
                + "INNER JOIN station AS start_station ON start_station.id = section.start_station_id"
                + "INNER JOIN station AS end_station ON end_station.id = section.end_station_id WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowDetailMapper, lineId);
    }
}
