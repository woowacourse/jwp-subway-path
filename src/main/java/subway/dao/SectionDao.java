package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;


    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Section> findByLineId(Long lineId) {
        String sql = "select s.id as id, s1.id as start_station_id, s1.name as start_station_name," +
                " s2.id as end_station_id, s2.name as end_station_name, s.distance as distance " +
                "from SECTION s join station s1 on s.start_station_id = s1.id " +
                "join station s2 on s.end_station_id = s2.id " +
                "where line_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Station station1 = new Station(rs.getLong("START_STATION_ID"), rs.getString("START_STATION_NAME"));
            Station station2 = new Station(rs.getLong("END_STATION_ID"), rs.getString("END_STATION_NAME"));
            Distance distance = new Distance(rs.getInt("DISTANCE"));
            return new Section(rs.getLong("ID"), station1, station2, distance);
        }, lineId);
    }

    public void deleteAllById(Long lineId) {
        String sql = "delete from SECTION where line_id = ?";

        jdbcTemplate.update(sql, lineId);
    }

    public void insertAll(Long lineId, List<Section> sections) {
        String sql = "insert into SECTION (line_id, start_station_id, end_station_id, distance) values (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, sections, sections.size(),
                (PreparedStatement preparedStatement, Section section) -> {
                    preparedStatement.setLong(1, lineId);
                    preparedStatement.setLong(2, section.getStartStation().getId());
                    preparedStatement.setLong(3, section.getEndStation().getId());
                    preparedStatement.setInt(4, section.getDistance().getDistance());
                });
    }

    public List<Section> findAll() {
        String sql = "select s1.id as start_station_id, s1.name as start_station_name, " +
                "s2.id as end_station_id, s2.name as end_station_name, s.distance " +
                "from SECTION s " +
                "join station s1 on s.start_station_id = s1.id " +
                "join station s2 on s.end_station_id = s2.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Station station1 = new Station(rs.getLong("start_station_id"), rs.getString("start_station_name"));
            Station station2 = new Station(rs.getLong("end_station_id"), rs.getString("end_station_name"));
            Distance distance = new Distance(rs.getInt("distance"));
            return new Section(station1, station2, distance);
        });
    }

    public Boolean findExistStationById(Long stationId) {
        String sql = "select exists (select * from section where start_station_id = ? OR end_station_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, stationId, stationId);
    }
}
