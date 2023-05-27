package subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;
import subway.entity.SectionStationEntity;

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

    public List<SectionStationEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT up.id as up_id, up.name as up_name, down.id as down_id, down.name as down_name, "
                + "section.distance as distance FROM section "
                + "INNER JOIN station AS up ON section.up_station_id = up.id "
                + "INNER JOIN station AS down ON section.down_station_id = down.id "
                + "WHERE line_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new SectionStationEntity(
                        lineId,
                        rs.getLong("up_id"),
                        rs.getString("up_name"),
                        rs.getLong("down_id"),
                        rs.getString("down_name"),
                        rs.getInt("distance")
                ), lineId);
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}

