package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Section;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionDao {
    public static final String LINE_ID = "line_id";
    public static final String CURRENT_STATION_ID = "current_station_id";
    public static final String NEXT_STATION_ID = "next_station_id";
    public static final String DISTANCE = "distance";
    public static final String ID = "id";
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public SectionDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SUBWAY_MAP")
                .usingGeneratedKeyColumns("ID");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int countStations(Line line) {
        String sql = "select count(*) from SUBWAY_MAP where line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, line.getId());
    }

    public long initialize(Section section) {
        if (countStations(section.getLine()) != 0) {
            throw new IllegalArgumentException("이미 초기 설정이 완료된 노선입니다.");
        }

        long savedId = insertAndReturnId(
                section.getLine().getId(),
                section.getPreviousStation().getId(),
                section.getNextStation().getId(),
                section.getDistance()
        );

        insertAndReturnId(
                section.getLine().getId(),
                section.getNextStation().getId(),
                null,
                0
        );

        return savedId;
    }

    public Section insert(Section section) {
        long id = insertAndReturnId(
                section.getLine().getId(),
                section.getPreviousStation().getId(),
                section.getNextStation().getId(),
                section.getDistance()
        );

        return Section.builder()
                .id(id)
                .line(section.getLine())
                .startingStation(section.getPreviousStation())
                .before(section.getNextStation())
                .distance(section.getDistance()).build();
    }

    private long insertAndReturnId(Long lineId, Long previousStationId, Long nextStationId, int distance) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource
                .addValue(LINE_ID, lineId)
                .addValue(CURRENT_STATION_ID, previousStationId)
                .addValue(NEXT_STATION_ID, nextStationId)
                .addValue(DISTANCE, distance);
        return simpleJdbcInsert
                .executeAndReturnKey(parameterSource)
                .longValue();
    }

    public void update(Section section) {
        jdbcTemplate.update("update SUBWAY_MAP set next_station_id = ?, distance = ? where id = ?",
                section.getNextStation().getId(),
                section.getDistance(),
                section.getId());
    }

    public boolean hasStation(Station station, Line line) {
        String sql = "select exists(select 1 from SUBWAY_MAP where current_station_id = ? and line_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, station.getId(), line.getId());
    }

    public Optional<Section> findByPreviousStation(Station previousStation, Line line) {
        String sql = "select * from SUBWAY_MAP MAP " +
                "inner join STATION S " +
                "on MAP.next_station_id = S.id " +
                "where MAP.current_station_id = ? and MAP.line_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, previousStation.getId(), line.getId());
        if (rowSet.next()) {
            return Optional.of(Section.builder()
                    .id(rowSet.getLong(ID))
                    .line(line)
                    .startingStation(previousStation)
                    .before(new Station(rowSet.getLong(NEXT_STATION_ID), rowSet.getString("name")))
                    .distance(rowSet.getInt(DISTANCE)).build());
        }
        return Optional.empty();
    }

    public Optional<Section> findByNextStation(Station nextStation, Line line) {
        String sql = "select * from SUBWAY_MAP MAP " +
                "inner join STATION S " +
                "on MAP.current_station_id = S.id " +
                "where MAP.next_station_id = ? and MAP.line_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, nextStation.getId(), line.getId());
        if (rowSet.next()) {
            return Optional.of(Section.builder()
                    .id(rowSet.getLong(ID))
                    .line(line)
                    .startingStation(new Station(rowSet.getLong(CURRENT_STATION_ID), rowSet.getString("name")))
                    .before(nextStation)
                    .distance(rowSet.getInt(DISTANCE)).build());
        }
        return Optional.empty();
    }

    public void deleteStation(Station station, Line line) {
        String sql = "delete from SUBWAY_MAP where current_station_id = ? and line_id = ?";

        if (countStations(line) == 2) {
            clearStations(line);
            return;
        }

        jdbcTemplate.update(sql, station.getId(), line.getId());
    }

    private void clearStations(Line line) {
        String sql = "delete from SUBWAY_MAP where line_id = ?";
        jdbcTemplate.update(sql, line.getId());
    }

    public List<Station> findAllOrderByUp(Line line) {
        String sql = "SELECT s.name as name, s.id as id " +
                "FROM SUBWAY_MAP t1 " +
                "JOIN SUBWAY_MAP t2 ON t1.next_station_id = t2.current_station_id " +
                "RIGHT OUTER JOIN STATION s ON t1.current_station_id = s.id " +
                "WHERE t1.line_id = ?" +
                "ORDER BY t1.current_station_id DESC, t2.current_station_id DESC;";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Station(rs.getLong("id"), rs.getString("name")),
                line.getId());
    }

    public boolean isHighestStationOfLine(Station station, Line line) {
        return hasStation(station, line) && findByNextStation(station, line).isEmpty();
    }
}
