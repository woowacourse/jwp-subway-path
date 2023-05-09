package subway.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.SectionRequest;

@Repository
public class SectionDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    public SectionDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }
    
    public Section insert(Section section) {
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(section);
        Long sectionId = insertAction.executeAndReturnKey(source).longValue();
        return new Section(sectionId, section.getLineId(), section.getUp(), section.getDown(), section.getDistance());
    }
}
