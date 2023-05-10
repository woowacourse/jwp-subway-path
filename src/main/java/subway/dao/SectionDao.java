package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.domain.Direction;
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
        String direction = rs.getString("direction");
        return new SectionEntity(id, distance, departure, arrival, lineId, Direction.getDirection(direction));
    };

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertSection(final Section section, final Long lineId) {
        final String sql = "INSERT INTO section(departure_id, arrival_id, distance, line_id, direction) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, section.getDepartureId(), section.getArrivalId(), section.getDistance(), lineId, section.getDirection().getValue());
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId);
    }

    public SectionEntity findByStationIds(final Long departureId, final Long arrivalId) {
        final String sql = "SELECT * FROM section WHERE departure_id = ? AND arrival_id = ?";

        return jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, departureId, arrivalId);
    }

    public void update(final Long id, final Section section) {
        final String sql = "UPDATE section SET distance = ?, departure_id = ?, arrival_id = ? WHERE id = ?";

        jdbcTemplate.update(sql, section.getDistance(), section.getDepartureId(), section.getArrivalId(), id);
    }
}
