package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.domain.Station;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;

@Repository
public class H2SectionDao implements SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Section> rowMapper = (rs, rowNum) -> {
        Long upwardId = convertToNullIfZero(rs.getLong("upward_id"));
        Long downwardId = convertToNullIfZero(rs.getLong("downward_id"));
        Station upward = Station.of(upwardId, rs.getString("upward_name"));
        Station downward = Station.of(downwardId, rs.getString("downward_name"));

        return Section.of(
                rs.getLong("id"),
                upward,
                downward,
                convertToNullIfZero(rs.getInt("distance"))
        );
    };

    public H2SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Long convertToNullIfZero(long id) {
        if (id == 0) {
            return null;
        }
        return id;
    }

    private Integer convertToNullIfZero(int distance) {
        if (distance == 0) {
            return null;
        }
        return distance;
    }

    @Override
    public long insert(Section section, long lineId) {
        String sql = "INSERT INTO SECTION(upward_id, downward_id, distance, line_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setObject(1, section.getUpward().getId(), Types.BIGINT);
            pst.setObject(2, section.getDownward().getId(), Types.BIGINT);
            pst.setObject(3, section.getDistance(), Types.INTEGER);
            pst.setLong(4, lineId);
            return pst;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Section> selectAll() {
        String sql = "SELECT s.id AS id, distance, upward_id, us.name AS upward_name, downward_id, ds.name AS downward_name " +
                "FROM SECTION AS s " +
                "LEFT JOIN STATION AS us ON s.upward_id = us.id " +
                "LEFT JOIN STATION AS ds ON s.downward_id = ds.id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<Section> selectSectionsByLineId(long lineId) {
        String sql = "SELECT s.id AS id, distance, upward_id, us.name AS upward_name, downward_id, ds.name AS downward_name " +
                "FROM SECTION AS s " +
                "LEFT JOIN STATION AS us ON s.upward_id = us.id " +
                "LEFT JOIN STATION AS ds ON s.downward_id = ds.id " +
                "WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    @Override
    public long deleteById(long id) {
        String sql = "DELETE FROM section WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public long deleteByLineId(long lineId){
        String sql = "DELETE FROM section WHERE line_id = ?";
        return jdbcTemplate.update(sql, lineId);
    }

}
