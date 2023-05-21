package subway.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.infrastructure.entity.SectionRow;

import java.util.List;

@Repository
public class SectionDao {

    private static final int BATCH_SIZE = 50;

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<SectionRow> rowMapper = (rs, cn) -> new SectionRow(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("up_bound"),
            rs.getLong("down_bound"),
            rs.getInt("distance")
    );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SectionRow> selectAllOfLineId(Long lineId) {
        String sectionSql = "select id, line_id, up_bound, down_bound, distance from section where line_id = ?";
        return jdbcTemplate.query(sectionSql, rowMapper, lineId);
    }

    public void insertAll(List<SectionRow> rows) {
        String sql = "insert into section (line_id, up_bound, down_bound, distance) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, rows, BATCH_SIZE,
                (ps, entity) -> {
                    ps.setLong(1, entity.getLineId());
                    ps.setLong(2, entity.getUpBound());
                    ps.setLong(3, entity.getDownBound());
                    ps.setInt(4, entity.getDistance());
                });
    }

    public void removeSections(Long lineId) {
        String sql = "delete from section where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public List<SectionRow> selectAll() {
        String sql = "select * from section";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
