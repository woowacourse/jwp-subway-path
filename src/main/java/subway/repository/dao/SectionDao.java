package subway.repository.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("section")
            .usingGeneratedKeyColumns("id");
    }

    public Section insert(final Section section, final long lindId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("distance", section.getDistance());
        params.put("line_id", lindId);
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        final Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE "
            + "FROM SECTION "
            + "WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public boolean containId(final Long id) {
        final String sql = "SELECT count(id) "
            + "FROM SECTION "
            + "WHERE line_id = ?";
        final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != 0;
    }
}
