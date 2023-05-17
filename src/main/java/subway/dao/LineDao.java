package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;

    public LineDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long insert(LineEntity lineEntity) {
        String sql = "INSERT INTO LINE (name, color, head_station) values(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, lineEntity.getName());
            ps.setString(2, lineEntity.getColor());
            ps.setLong(3, lineEntity.getHeadStationId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
