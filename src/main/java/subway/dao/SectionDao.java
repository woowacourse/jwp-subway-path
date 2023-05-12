package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;

import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        int distance = rs.getInt("distance");
        Long departure = rs.getLong("departure_id");
        Long arrival = rs.getLong("arrival_id");
        Long lineId = rs.getLong("line_id");
        return new SectionEntity(id, distance, departure, arrival, lineId);
    };

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertSection(final Section section) {
        final String sql = "INSERT INTO section(departure_id, arrival_id, distance, line_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, section.getDeparture().getId(), section.getArrival().getId(), section.getDistance(), section.getLine().getId());
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId);
    }

    public SectionEntity findByStationIds(final Long departureId, final Long arrivalId) {
        final String sql = "SELECT * FROM section WHERE departure_id = ? AND arrival_id = ?";

        return jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, departureId, arrivalId);
    }

    public void update(final Section before, final Section after) {
        final String sql = "UPDATE section SET distance = ?, departure_id = ?, arrival_id = ? WHERE departure_id = ? AND arrival_id = ?";

        jdbcTemplate.update(sql, after.getDistance(), after.getDeparture().getId(), after.getArrival().getId(), before.getDeparture().getId(), before.getArrival().getId());
    }

    public List<SectionEntity> findAll() {
        final String sql = "SELECT * FROM section";

        return jdbcTemplate.query(sql, sectionEntityRowMapper);
    }

    public void deleteAll() {
        String sql = "DELETE FROM section";

        jdbcTemplate.update(sql);
    }
}
