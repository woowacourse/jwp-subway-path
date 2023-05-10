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

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(SectionEntity sectionEntity) {
        String sql = "INSERT INTO subway_section (up_station_id, down_station_id, line_id, distance) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setLong(1, sectionEntity.getUpStationId());
            preparedStatement.setLong(2, sectionEntity.getDownStationId());
            preparedStatement.setLong(3, sectionEntity.getLineId());
            preparedStatement.setInt(4, sectionEntity.getDistance());

            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Optional<SectionEntity> findByUpStationIdAndLindId(Long upStationId, Long lineId) {
        String sql = "SELECT id, up_station_id, down_station_id, line_id, distance " +
                "FROM subway_section WHERE up_station_id = ? AND line_id = ?";
        List<SectionEntity> sectionEntities = jdbcTemplate.query(sql,
                sectionEntityRowMapper(), upStationId, lineId
        );
        return sectionEntities.stream().findAny();
    }

    private RowMapper<SectionEntity> sectionEntityRowMapper() {
        return (rs, rowNum) ->
                new SectionEntity(
                        rs.getLong("id"),
                        rs.getLong("up_station_id"),
                        rs.getLong("down_station_id"),
                        rs.getLong("line_id"),
                        rs.getInt("distance")
                );
    }

    public void deleteBySectionId(Long id) {

    }

    public Optional<SectionEntity> findByDownStationIdAndLindId(Long downStationId, Long lineId) {
        String sql = "SELECT id, up_station_id, down_station_id, line_id, distance " +
                "FROM subway_section WHERE down_station_id = ? AND line_id = ?";
        List<SectionEntity> sectionEntities = jdbcTemplate.query(sql,
                sectionEntityRowMapper(), downStationId, lineId
        );
        return sectionEntities.stream().findAny();
    }
}
