package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.util.Optional;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("subway_section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(SectionEntity sectionEntity) {
        return 1L;
    }

    public Optional<SectionEntity> findByUpStationId(Long upStationId) {
        return null;
    }

    public void deleteBySectionId(Long id) {

    }

    public Optional<SectionEntity> findByDownStationId(Long currentDownStationId) {
        return null;
    }
}
