package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.section.general.NearbyStations;
import subway.domain.section.transfer.TransferSection;
import subway.domain.station.Station;

@Repository
public class TransferSectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public TransferSectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("transfer_section").usingGeneratedKeyColumns("id");
    }

    public TransferSection insert(TransferSection transferSection) {
        Station upStation = transferSection.getUpStation();
        Station downStation = transferSection.getDownStation();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("up_station_id", upStation.getId())
                .addValue("down_station_id", downStation.getId());

        NearbyStations nearbyStations =
                NearbyStations.createByUpStationAndDownStation(upStation, downStation);

        long insertedId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new TransferSection(insertedId, nearbyStations);
    }
}
