package subway.dao;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineSection;

@Repository
public class LineSectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;


    public LineSectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line_section")
            .usingGeneratedKeyColumns("id");
    }

    public LineSection insert(final LineSection lineSection) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", lineSection.getLineId());
        params.put("section_id", lineSection.getSectionId());

        Long lineSectionId = insertAction.executeAndReturnKey(params).longValue();
        return new LineSection(lineSectionId, lineSection.getLineId(), lineSection.getSectionId());
    }
}
