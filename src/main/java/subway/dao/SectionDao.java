package subway.dao;

import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

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
        final String sql = "SELECT up_station_id, down_station_id, distance FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new SectionEntity(
                        lineId,
                        rs.getLong("up_station_id"),
                        rs.getLong("down_station_id"),
                        rs.getInt("distance")
                ), lineId);
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}

