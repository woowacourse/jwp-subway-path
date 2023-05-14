package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SectionEntity> sectionEntityRowMapper =
            (rs, rowNum) -> new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance"),
                    rs.getLong("line_id"));

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public SectionEntity insert(SectionEntity sectionEntity) {
        String sql = "INSERT INTO section (up_station_id, down_station_id, distance, line_id) VALUES (?, ?, ?, ?)";
        Long upStationId = sectionEntity.getUpStationId();
        Long downStationId = sectionEntity.getDownStationId();
        Long lineId = sectionEntity.getLineId();
        int distance = sectionEntity.getDistance();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setLong(1, upStationId);
            preparedStatement.setLong(2, downStationId);
            preparedStatement.setInt(3, distance);
            preparedStatement.setLong(4, lineId);

            return preparedStatement;
        }, keyHolder);

        long insertedId = keyHolder.getKey().longValue();
        return new SectionEntity(insertedId, upStationId, downStationId, distance, lineId);
    }

    public List<SectionEntity> findContainingSections(Long newUpStationId, Long newDownStationId) {
        String sql = "SELECT id, up_station_id, down_station_id, distance, line_id " +
                "FROM section " +
                "WHERE up_station_id = ? OR up_station_id = ? OR down_station_id = ? OR down_station_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper,
                newUpStationId, newDownStationId, newUpStationId, newDownStationId);
    }

    public Optional<SectionEntity> findByUpStationIdAndLindId(Long upStationId, Long lineId) {
        String sql = "SELECT id, up_station_id, down_station_id, distance, line_id " +
                "FROM section WHERE up_station_id = ? AND line_id = ?";

        List<SectionEntity> sectionEntities = jdbcTemplate.query(sql, sectionEntityRowMapper, upStationId, lineId);
        return sectionEntities.stream().findAny();
    }

    public Optional<SectionEntity> findByDownStationIdAndLindId(Long downStationId, Long lineId) {
        String sql = "SELECT id, up_station_id, down_station_id, distance, line_id " +
                "FROM section WHERE down_station_id = ? AND line_id = ?";

        List<SectionEntity> sectionEntities = jdbcTemplate.query(sql, sectionEntityRowMapper, downStationId, lineId);
        return sectionEntities.stream().findAny();
    }

    public List<SectionEntity> findSectionEntitiesByLineId(Long lineId) {
        String sql = "SELECT id, up_station_id, down_station_id, distance, line_id " +
                "FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId);
    }

    public void deleteBySectionId(Long id) {
        String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
