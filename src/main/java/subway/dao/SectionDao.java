package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SectionEntity> sectionMapper = (rs, cn) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getString("up"),
            rs.getString("down"),
            rs.getInt("distance")
    );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SectionEntity> findById(Long lineId) {
        String sectionSql = "select id, line_id, up, down, distance from section where line_id = ?";
        return jdbcTemplate.query(sectionSql, sectionMapper, lineId);
    }

    public void save(List<SectionEntity> sectionEntities) {
        if (sectionEntities.isEmpty()) {
            return;
        }
        long lineId = findLineId(sectionEntities);
        String deleteSql = "delete from section where line_id = ?";
        String insertSql = "insert into section (line_id, up, down, distance) values (?, ?, ?, ?)";

        jdbcTemplate.update(deleteSql, lineId);

        jdbcTemplate.batchUpdate(insertSql, sectionEntities, 50,
                (ps, entity) -> {
                    ps.setLong(1, entity.getLineId());
                    ps.setString(2, entity.getLeft());
                    ps.setString(3, entity.getRight());
                    ps.setInt(4, entity.getDistance());
                });
    }

    private long findLineId(final List<SectionEntity> sectionEntities) {
        final long idsInEntities = sectionEntities.stream()
                .mapToLong(SectionEntity::getLineId)
                .distinct()
                .count();
        if (idsInEntities != 1) {
            throw new IllegalArgumentException("같은 라인에 대한 section 저장 정보만 보내야합니다.");
        }

        return sectionEntities.get(0).getLineId();
    }
}
