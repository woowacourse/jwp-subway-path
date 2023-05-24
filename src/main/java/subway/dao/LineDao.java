package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    public List<StationEntity> findAllStationsById(Long lineId) {
        String sql = "SELECT * FROM STATION WHERE line_id = ?";

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    Long nextStationId = resultSet.getLong("next_station");
                    int distance = resultSet.getInt("distance");
                    Long entityLineId = resultSet.getLong("line_id");

                    return new StationEntity(id, name, nextStationId, distance, entityLineId);
                }, lineId);
    }

    public List<LineEntity> findAllLines() {
        String sql = "SELECT * FROM LINE";

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> {
                    Long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String color = resultSet.getString("color");
                    Long headStationId = resultSet.getLong("head_station");

                    return new LineEntity(id, name, color, headStationId);
                });
    }

    public Long update(Long id, LineRequest lineRequest) {
        String sql = "UPDATE LINE SET name = ?, color = ? WHERE id = ?";

        return Long.valueOf(jdbcTemplate.update(sql, lineRequest.getName(), lineRequest.getColor(), id));
    }

    public Long updateHeadStation(Long id, Station upStation) {
        String sql = "UPDATE LINE SET head_station = ? WHERE id = ?";

        return Long.valueOf(jdbcTemplate.update(sql, upStation.getId(), id));
    }

    public Optional<LineEntity> findLineEntityById(Long id) {
        String sql = "SELECT * FROM LINE WHERE id = ?";
        LineEntity lineEntity = jdbcTemplate.queryForObject(sql, getLineRowMapper(), id);
        return Optional.of(lineEntity);
    }

    public Optional<LineEntity> findLineEntityByName(String name) {
        String sql = "SELECT * FROM LINE WHERE name = ?";
        LineEntity lineEntity = jdbcTemplate.queryForObject(sql, getLineRowMapper(), name);
        return Optional.of(lineEntity);
    }

    private RowMapper<LineEntity> getLineRowMapper() {
        return (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String color = resultSet.getString("color");
            Long headStation = resultSet.getLong("head_station");

            return new LineEntity(id, name, color, headStation);
        };
    }

    public Long remove(Long id) {
        String query = "DELETE FROM LINE WHERE id = ?";
        return Long.valueOf(jdbcTemplate.update(query, id));
    }

    public void removeAll() {
        String query = "DELETE FROM STATION";
        jdbcTemplate.update(query);
    }

}
