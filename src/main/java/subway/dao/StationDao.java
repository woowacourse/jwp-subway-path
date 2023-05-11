package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.station.Station;
import subway.entity.StationEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(StationEntity stationEntity) {
        String sql = "INSERT INTO station (name, line_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, stationEntity.getName());
            preparedStatement.setLong(2, stationEntity.getLineId());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "select id, name, line_id from STATION where id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, stationEntityRowMapper(), id));
    }

    public Optional<StationEntity> findByStationNameAndLineName(String stationName, String lineName) {
        String sql = "SELECT station.id, station.name, line.id FROM LINE " +
                "INNER JOIN STATION " +
                "ON line.id = station.line_id " +
                "WHERE station.name = ? AND line.name = ?";

        List<StationEntity> stationEntities = jdbcTemplate.query(sql,
                stationEntityRowMapper(),
                stationName, lineName);
        return stationEntities.stream()
                .findAny();
    }

    private RowMapper<StationEntity> stationEntityRowMapper() {
        return (rs, rowNum) -> {
            long findStationId = rs.getLong("station.id");
            String findStationName = rs.getString("name");
            long findLineId = rs.getLong("line.id");
            return new StationEntity(findStationId, findStationName, findLineId);
        };
    }

    public void update(Station newStation) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newStation.getName()});
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
