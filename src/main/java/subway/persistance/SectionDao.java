package subway.persistance;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.persistance.entity.SectionEntity;

import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rowNum) -> {
        Long sectionId = rs.getLong("section_id");
        Long upStationId = rs.getLong("up_station_id");
        Long downStationId = rs.getLong("down_station_id");
        int distance = rs.getInt("distance");
        Long lineId = rs.getLong("line_id");
        int listOrder = rs.getInt("list_order");
        return new SectionEntity(sectionId, upStationId, downStationId, distance, lineId, listOrder);
    };

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertSection(final SectionEntity entity) {
        final String sql = "INSERT INTO section(up_station_id, down_station_id, distance, line_id, list_order) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, entity.getUpStationId(), entity.getDownStationId(), entity.getDistance(), entity.getLineId(), entity.getListOrder());
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT " +
                "section_id, " +
                "up_station_id, " +
                "down_station_id," +
                " distance, line_id, " +
                "list_order " +
                "FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId);
    }

    public void deleteByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";

        jdbcTemplate.update(sql, lineId);
    }
}
