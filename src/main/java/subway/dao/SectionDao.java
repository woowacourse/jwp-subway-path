package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.SectionWithStationNameEntity;
import subway.dao.entity.StationEntity;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getLong("line_id"),
                    rs.getInt("distance")
            );

    private final RowMapper<SectionWithStationNameEntity> sectionWithStationNameEntityRowMapper = (rs, rowNum) ->
            new SectionWithStationNameEntity(
                    rs.getLong("section.id"),
                    new StationEntity(rs.getLong("us.id"), rs.getString("us.name")),
                    new StationEntity(rs.getLong("ds.id"), rs.getString("ds.name")),
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

    public List<SectionWithStationNameEntity> findByLineIdWithStationName(final Long lineId) {
        final String sql = "SELECT * FROM section "
                + "JOIN station AS us ON section.up_station_id = us.id "
                + "JOIN station AS ds ON section.up_station_id = ds.id "
                + "WHERE line_id = ?";
        return jdbcTemplate.query(sql, sectionWithStationNameEntityRowMapper, lineId);
    }

    public int delete(final SectionEntity sectionEntity) {
        final String sql = "DELETE FROM section WHERE up_station_id = ? AND down_station_id = ? AND line_id = ?";
        return jdbcTemplate.update(
                sql,
                sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(),
                sectionEntity.getLineId()
        );
    }

    public int deleteByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        return jdbcTemplate.update(sql, lineId);
    }
}
