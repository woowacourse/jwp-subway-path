package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.SubwayMap;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class SubwayMapDao {
    public static final String LINE_ID = "line_id";
    public static final String CURRENT_STATION_ID = "current_station_id";
    public static final String NEXT_STATION_ID = "next_station_id";
    public static final String DISTANCE = "distance";
    public static final String ID = "id";
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public SubwayMapDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SUBWAY_MAP")
                .usingGeneratedKeyColumns("ID");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int countStations(Line line) {
        String sql = "select count(*) from SUBWAY_MAP where line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, line.getId());
    }

    public void initialize(SubwayMap subwayMap) {
        if (countStations(subwayMap.getLine()) != 0) {
            throw new IllegalArgumentException("이미 초기 설정이 완료된 노선입니다.");
        }

        insertAndReturnId(
                subwayMap.getLine().getId(),
                subwayMap.getPreviousStation().getId(),
                subwayMap.getNextStation().getId(),
                subwayMap.getDistance()
        );

        insertAndReturnId(
                subwayMap.getLine().getId(),
                subwayMap.getNextStation().getId(),
                null,
                0
        );
    }

    public SubwayMap insert(SubwayMap subwayMap) {
        long id = insertAndReturnId(
                subwayMap.getLine().getId(),
                subwayMap.getPreviousStation().getId(),
                subwayMap.getNextStation().getId(),
                subwayMap.getDistance()
        );

        return SubwayMap.builder()
                .id(id )
                .line(subwayMap.getLine())
                .startingStation(subwayMap.getPreviousStation())
                .before(subwayMap.getNextStation())
                .distance(subwayMap.getDistance())
                .build();
    }

    public void update(SubwayMap subwayMap) {
        jdbcTemplate.update("update SUBWAY_MAP set next_station_id = ?, distance = ? where id = ?",
                subwayMap.getNextStation().getId(),
                subwayMap.getDistance(),
                subwayMap.getId());
    }

    private long insertAndReturnId(Long id, Long startingStationId, Long destinationStationId, int distance) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource
                .addValue(LINE_ID, id)
                .addValue(CURRENT_STATION_ID, startingStationId)
                .addValue(NEXT_STATION_ID, destinationStationId)
                .addValue(DISTANCE, distance);
        return simpleJdbcInsert
                .executeAndReturnKey(parameterSource)
                .longValue();
    }

    public boolean existsStation(Station station, Line line) {
        String sql = "select exists(select 1 from SUBWAY_MAP where current_station_id = ? and line_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, station.getId(), line.getId());
    }

    public int findDistanceBetween(Station stationA, Station stationB, Line line) {
        // 두 역이 current, next 나란히 있는 경우
        String sql = "select * from SUBWAY_MAP where current_station_id = ? and line_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, stationA.getId(), line.getId());
        if (rowSet.next()) {
            long nextStationId = rowSet.getLong(NEXT_STATION_ID);
            if (nextStationId == stationB.getId()) {
                return rowSet.getInt(DISTANCE);
            }
        }

        // 두 역이 next, current 이렇게 나란히 있는 경우
        SqlRowSet reversedRowSet = jdbcTemplate.queryForRowSet(sql, stationB.getId(), line.getId());
        if (reversedRowSet.next()) {
            long reversedNextStationId = reversedRowSet.getLong(NEXT_STATION_ID);
            if (reversedNextStationId == stationA.getId()) {
                return reversedRowSet.getInt(DISTANCE);
            }
        }

        throw new IllegalArgumentException("주어진 두 역이 이웃한 역이 아닙니다.");
        // TODO: 아직까지 이웃하지 않은 역의 거리를 조회하지는 않는다고 가정합니다.
    }

    public long findStationIdByNextStation(Station nextStation) {
        String sql = "select current_station_id from SUBWAY_MAP where next_station_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, nextStation.getId());
    }

    public Optional<SubwayMap> findByPreviousStation(Station previousStation, Line line) {
        String sql = "select * from SUBWAY_MAP MAP " +
                "inner join STATION S " +
                "on MAP.next_station_id = S.id " +
                "where MAP.current_station_id = ? and MAP.line_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, previousStation.getId(), line.getId());
        if (rowSet.next()) {
            return Optional.of(SubwayMap.builder()
                    .id(rowSet.getLong(ID))
                    .line(line)
                    .startingStation(previousStation)
                    .before(new Station(rowSet.getLong(NEXT_STATION_ID), rowSet.getString("name")))
                    .distance(rowSet.getInt(DISTANCE))
                    .build());
        }
        return Optional.empty();
    }

    public Optional<SubwayMap> findByNextStation(Station nextStation, Line line) {
        String sql = "select * from SUBWAY_MAP MAP " +
                "inner join STATION S " +
                "on MAP.current_station_id = S.id " +
                "where MAP.next_station_id = ? and MAP.line_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, nextStation.getId(), line.getId());
        if (rowSet.next()) {
            return Optional.of(SubwayMap.builder()
                    .id(rowSet.getLong(ID))
                    .line(line)
                    .startingStation(new Station(rowSet.getLong(CURRENT_STATION_ID), rowSet.getString("name")))
                    .before(nextStation)
                    .distance(rowSet.getInt(DISTANCE))
                    .build());
        }
        return Optional.empty();
    }
}
