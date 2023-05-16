package subway.dao;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("line_id"),
                    rs.getObject("up_station_id", Long.class),
                    rs.getObject("down_station_id", Long.class),
                    rs.getInt("distance")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertAll(final List<SectionEntity> sectionEntities) {
        final String sql = "INSERT INTO section VALUES (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, sectionEntities, sectionEntities.size(),
                (PreparedStatement preparedStatement, SectionEntity sectionEntity) -> {
                    preparedStatement.setLong(1, sectionEntity.getLineId());
                    preparedStatement.setLong(2, sectionEntity.getUpStationId());
                    preparedStatement.setLong(3, sectionEntity.getDownStationId());
                    preparedStatement.setInt(4, sectionEntity.getDistance());
                });
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}

