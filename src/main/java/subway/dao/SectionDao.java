package subway.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
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
    private static final int EXISTED_SECTION = 1;
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
                    rs.getLong("id"),
                    new StationEntity(
                            rs.getLong("up_station_id"),
                            rs.getString("up_station_name")
                    ),
                    new StationEntity(
                            rs.getLong("down_station_id"),
                            rs.getString("down_station_name")
                    ),
                    rs.getInt("distance")
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

    public boolean exists(final SectionEntity sectionEntity) {
        final String sql = "SELECT EXISTS("
                + "SELECT * FROM section "
                + "WHERE line_id = ? AND up_station_id = ? AND down_station_id = ?"
                + ")";

        Integer result = jdbcTemplate.queryForObject(
                sql, Integer.class,
                sectionEntity.getLineId(),
                sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId()
        );

        return result == EXISTED_SECTION;
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";

        try {
            return jdbcTemplate.query(sql, rowMapper, lineId);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public Optional<SectionWithStationNameEntity> findByLineIdAndUpStationEntityAndDownStationEntity(
            final Long upStationId,
            final Long downStationId,
            final Long lineId
    ) {
        final String sql = "SELECT s.id AS id, "
                + "s.up_station_id AS up_station_id, "
                + "us.name AS up_station_name, "
                + "s.down_station_id AS down_station_id, "
                + "ds.name AS down_station_name, "
                + "s.line_id AS line_id, "
                + "s.distance AS distance "
                + "FROM section s "
                + "JOIN station us ON s.up_station_id = us.id "
                + "JOIN station ds ON s.down_station_id = ds.id "
                + "WHERE s.up_station_id = ? AND s.down_station_id = ? AND s.line_id = ?";

        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, sectionWithStationNameEntityRowMapper, upStationId, downStationId,
                            lineId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<SectionWithStationNameEntity> findBySectionIdWithStationName(final Long sectionId) {
        final String sql = "SELECT s.id AS id, "
                + "s.distance AS distance, "
                + "us.id AS up_station_id, "
                + "ds.id AS down_station_id, "
                + "us.name AS up_station_name, "
                + "ds.name AS down_station_name "
                + "FROM section s "
                + "JOIN station us ON s.up_station_id = us.id "
                + "JOIN station ds ON s.down_station_id = ds.id "
                + "WHERE s.id = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, sectionWithStationNameEntityRowMapper, sectionId)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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
